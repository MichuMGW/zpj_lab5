package com.example.library.library.controller;

import com.example.library.library.entity.Reader;
import com.example.library.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    // POST /api/readers
    @PostMapping
    public ResponseEntity<String> createReader(@RequestBody Reader reader) {
        try {
            int result = readerService.save(reader);
            if (result > 0) {
                return new ResponseEntity<>("Klient zapisany pomyślnie.", HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Nie udało się zapisać klienta.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Błąd serwera podczas zapisu: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET /api/readers
    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        List<Reader> readers = readerService.findAll();
        if (readers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(readers, HttpStatus.OK);
    }
    // GET /api/readers/active
    @GetMapping("/active")
    public ResponseEntity<List<Reader>> getAllActiveReaders() {
        List<Reader> readers = readerService.findAllActive();
        if (readers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(readers, HttpStatus.OK);
    }

    // GET /api/readers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable int id) {
        return readerService.findById(id)
                .map(reader -> new ResponseEntity<>(reader, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT /api/readers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateReader(@PathVariable int id, @RequestBody Reader readerDetails) {

        int result = readerService.update(id, readerDetails);

        if (result == -1) {
            return new ResponseEntity<>("Klient o podanym ID nie istnieje.", HttpStatus.NOT_FOUND);
        } else if (result > 0) {
            return new ResponseEntity<>("Dane klienta zaktualizowane pomyślnie.", HttpStatus.OK);
        }

        return new ResponseEntity<>("Błąd aktualizacji lub brak zmian.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // DELETE /api/readers/soft/{id}
    @DeleteMapping("/soft/{id}")
    public ResponseEntity<String> softDeleteReader(@PathVariable int id) {
        int result = readerService.softDelete(id); // Używamy serwisu
        if (result > 0) {
            return new ResponseEntity<>("Klient oznaczony jako nieaktywny (Soft Delete).", HttpStatus.OK);
        }
        return new ResponseEntity<>("Nie znaleziono klienta do dezaktywacji.", HttpStatus.NOT_FOUND);
    }

    // DELETE /api/readers/hard/{id}
    @DeleteMapping("/hard/{id}")
    public ResponseEntity<String> hardDeleteReader(@PathVariable int id) {
        int result = readerService.hardDelete(id);
        if (result > 0) {
            return new ResponseEntity<>("Klient permanentnie usunięty (Hard Delete).", HttpStatus.OK);
        }
        return new ResponseEntity<>("Nie znaleziono klienta do usunięcia.", HttpStatus.NOT_FOUND);
    }
}