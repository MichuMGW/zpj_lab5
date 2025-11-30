package com.example.library.library.service;

import com.example.library.library.entity.Book;
import com.example.library.library.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testy jednostkowe BookService")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book(1, "Title", "Author", 2000);
    }

    // === findAll() ===
    @Test
    @DisplayName("findAll() - zwraca listę książek z repozytorium")
    void findAll_returnsBooksFromRepository() {
        List<Book> books = List.of(sampleBook, new Book(2, "T2", "A2", 2020));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.findAll();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Title", result.get(0).getTitle())
        );
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll() - zwraca pustą listę gdy brak danych")
    void findAll_returnsEmptyListWhenNoBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> result = bookService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository).findAll();
    }

    // === findById(int) ===
    @Test
    @DisplayName("findById() - zwraca książkę gdy istnieje")
    void findById_returnsBookWhenExists() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook));

        Optional<Book> result = bookService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(sampleBook, result.get());
        verify(bookRepository).findById(1);
    }

    @Test
    @DisplayName("findById() - zwraca Optional.empty() gdy książka nie istnieje")
    void findById_returnsEmptyWhenNotFound() {
        when(bookRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.findById(999);

        assertFalse(result.isPresent());
        verify(bookRepository).findById(999);
    }

    // === save(Book) ===
    @Test
    @DisplayName("save() - przekazuje książkę do repozytorium i zwraca wartość update()")
    void save_delegatesToRepository() {
        when(bookRepository.save(sampleBook)).thenReturn(1);

        int result = bookService.save(sampleBook);

        assertEquals(1, result);
        verify(bookRepository).save(sampleBook);
    }

    @Test
    @DisplayName("save() - propaguje wyjątek z repozytorium (sytuacja niepożądana)")
    void save_propagatesRepositoryException() {
        when(bookRepository.save(sampleBook))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookService.save(sampleBook));

        assertEquals("DB error", ex.getMessage());
        verify(bookRepository).save(sampleBook);
    }

    // === update(int id, Book book) ===
    @Nested
    @DisplayName("update() - testy zagnieżdżone")
    class UpdateTests {

        @Test
        @DisplayName("update() - nadpisuje ID z parametru i wywołuje repozytorium")
        void update_setsIdFromArgumentAndDelegates() {
            Book toUpdate = new Book(999, "Testowy tytuł", "Testowy autor", 1990);
            when(bookRepository.update(any(Book.class))).thenReturn(1);

            int result = bookService.update(5, toUpdate);

            assertEquals(1, result);
            ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
            verify(bookRepository).update(captor.capture());
            Book passed = captor.getValue();
            assertEquals(5, passed.getId());
        }

        @Test
        @DisplayName("update() - zwraca wynik repozytorium gdy nic nie zaktualizowano (0)")
        void update_returnsZeroWhenRepositoryUpdatesNothing() {
            when(bookRepository.update(any(Book.class))).thenReturn(0);

            int result = bookService.update(1, sampleBook);

            assertEquals(0, result);
            verify(bookRepository).update(any(Book.class));
        }
    }

    // === hardDelete(int) ===
    @ParameterizedTest(name = "hardDelete({0}) - zwraca wynik repozytorium")
    @ValueSource(ints = {0, 1, 5})
    @DisplayName("hardDelete() - różne wartości zwrotne repozytorium")
    void hardDelete_returnsRepositoryResult(int repoResult) {
        when(bookRepository.hardDelete(10)).thenReturn(repoResult);

        int result = bookService.hardDelete(10);

        assertEquals(repoResult, result);
        verify(bookRepository).hardDelete(10);
    }

    @Test
    @DisplayName("hardDelete() - propaguje wyjątek z repozytorium")
    void hardDelete_propagatesException() {
        when(bookRepository.hardDelete(1))
                .thenThrow(new IllegalStateException("DB locked"));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> bookService.hardDelete(1));

        assertEquals("DB locked", ex.getMessage());
        verify(bookRepository).hardDelete(1);
    }
}
