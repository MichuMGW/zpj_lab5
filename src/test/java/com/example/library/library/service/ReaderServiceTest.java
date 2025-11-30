package com.example.library.library.service;

import com.example.library.library.entity.Reader;
import com.example.library.library.repository.ReaderRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testy jednostkowe ReaderService")
class ReaderServiceTest {

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private ReaderService readerService;

    private Reader activeReader;
    private Reader inactiveReader;

    @BeforeEach
    void setUp() {
        activeReader = new Reader(1, "Jan Kowalski", "jan@example.com", true);
        inactiveReader = new Reader(2, "Adam Nowak", "adam@example.com", false);
    }

    // === save(Reader) ===
    @Test
    @DisplayName("save() - poprawnie zapisuje aktywnego klienta")
    void save_savesActiveReader() {
        when(readerRepository.save(activeReader)).thenReturn(1);

        int result = readerService.save(activeReader);

        assertEquals(1, result);
        verify(readerRepository).save(activeReader);
    }

    @Test
    @DisplayName("save() - propaguje wyjątek z repozytorium (np. błąd DB)")
    void save_propagatesException() {
        when(readerRepository.save(any(Reader.class)))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> readerService.save(activeReader));

        assertEquals("DB error", ex.getMessage());
        verify(readerRepository).save(activeReader);
    }

    // === findById(int) ===
    @Test
    @DisplayName("findById() - zwraca czytelnika gdy istnieje")
    void findById_returnsReaderWhenExists() {
        when(readerRepository.findById(1)).thenReturn(Optional.of(activeReader));

        Optional<Reader> result = readerService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(activeReader, result.get());
        verify(readerRepository).findById(1);
    }

    @Test
    @DisplayName("findById() - zwraca Optional.empty() gdy klient nie istnieje")
    void findById_returnsEmptyWhenNotFound() {
        when(readerRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Reader> result = readerService.findById(999);

        assertFalse(result.isPresent());
        verify(readerRepository).findById(999);
    }

    // === findAll() ===
    @Test
    @DisplayName("findAll() - zwraca listę wszystkich czytelników")
    void findAll_returnsAllReaders() {
        when(readerRepository.findAll()).thenReturn(List.of(activeReader, inactiveReader));

        List<Reader> result = readerService.findAll();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Jan Kowalski", result.get(0).getName())
        );
        verify(readerRepository).findAll();
    }

    @Test
    @DisplayName("findAll() - zwraca pustą listę gdy brak danych")
    void findAll_returnsEmptyListWhenNoReaders() {
        when(readerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Reader> result = readerService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(readerRepository).findAll();
    }

    // === findAllActive() ===
    @Test
    @DisplayName("findAllActive() - zwraca tylko aktywnych czytelników (delegacja do repozytorium)")
    void findAllActive_returnsOnlyActiveReaders() {
        when(readerRepository.findAllActive()).thenReturn(List.of(activeReader));

        List<Reader> result = readerService.findAllActive();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
        verify(readerRepository).findAllActive();
    }

    @Test
    @DisplayName("findAllActive() - zwraca pustą listę gdy brak aktywnych")
    void findAllActive_returnsEmptyWhenNoActiveReaders() {
        when(readerRepository.findAllActive()).thenReturn(Collections.emptyList());

        List<Reader> result = readerService.findAllActive();

        assertTrue(result.isEmpty());
        verify(readerRepository).findAllActive();
    }

    // === update(int id, Reader readerDetails) ===
    @Test
    @DisplayName("update() - zwraca -1 gdy czytelnik o ID nie istnieje (warunek brzegowy)")
    void update_returnsMinusOneWhenReaderDoesNotExist() {
        when(readerRepository.findById(999)).thenReturn(Optional.empty());

        int result = readerService.update(999, activeReader);

        assertEquals(-1, result);
        verify(readerRepository).findById(999);
        verify(readerRepository, never()).update(any(Reader.class));
    }

    @Test
    @DisplayName("update() - aktualizuje istniejącego czytelnika i zwraca wynik repozytorium")
    void update_updatesExistingReader() {
        when(readerRepository.findById(1)).thenReturn(Optional.of(activeReader));
        when(readerRepository.update(any(Reader.class))).thenReturn(1);

        int result = readerService.update(1, inactiveReader);

        assertEquals(1, result);

        ArgumentCaptor<Reader> captor = ArgumentCaptor.forClass(Reader.class);
        verify(readerRepository).update(captor.capture());
        Reader updated = captor.getValue();

        assertAll(
                () -> assertEquals(1, updated.getId()),
                () -> assertEquals(inactiveReader.getName(), updated.getName()),
                () -> assertEquals(inactiveReader.getEmail(), updated.getEmail()),
                () -> assertEquals(inactiveReader.isActive(), updated.isActive())
        );
    }

    @Test
    @DisplayName("update() - gdy repozytorium zwraca 0, serwis zwraca 0 (brak modyfikacji)")
    void update_returnsZeroWhenRepositoryReturnsZero() {
        when(readerRepository.findById(1)).thenReturn(Optional.of(activeReader));
        when(readerRepository.update(any(Reader.class))).thenReturn(0);

        int result = readerService.update(1, activeReader);

        assertEquals(0, result);
        verify(readerRepository).update(any(Reader.class));
    }

    // === softDelete(int) ===
    @Test
    @DisplayName("softDelete() - deleguje do repozytorium i zwraca wynik")
    void softDelete_delegatesToRepository() {
        when(readerRepository.softDelete(1)).thenReturn(1);

        int result = readerService.softDelete(1);

        assertEquals(1, result);
        verify(readerRepository).softDelete(1);
    }

    @Test
    @DisplayName("softDelete() - gdy brak rekordu, zwraca 0 (warunek brzegowy)")
    void softDelete_returnsZeroWhenNoReader() {
        when(readerRepository.softDelete(999)).thenReturn(0);

        int result = readerService.softDelete(999);

        assertEquals(0, result);
        verify(readerRepository).softDelete(999);
    }

    // === hardDelete(int) ===
    @Test
    @DisplayName("hardDelete() - poprawnie usuwa istniejącego czytelnika")
    void hardDelete_delegatesToRepository() {
        when(readerRepository.hardDelete(1)).thenReturn(1);

        int result = readerService.hardDelete(1);

        assertEquals(1, result);
        verify(readerRepository).hardDelete(1);
    }

    @Test
    @DisplayName("hardDelete() - propaguje wyjątek z repozytorium (np. constraint violation)")
    void hardDelete_propagatesException() {
        when(readerRepository.hardDelete(1))
                .thenThrow(new IllegalStateException("FK violation"));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> readerService.hardDelete(1));

        assertEquals("FK violation", ex.getMessage());
        verify(readerRepository).hardDelete(1);
    }
}
