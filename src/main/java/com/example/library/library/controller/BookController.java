package com.example.library.library.controller;

import com.example.library.library.entity.Book;
import com.example.library.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepositor;

    @Autowired
    public BookController(BookRepository bookRepositor) {
        this.bookRepositor = bookRepositor;
    }

    // GET /api/books
    @GetMapping
    public List<Book> findAll() {
        return bookRepositor.findAll();
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public Book findById(@PathVariable int id) {
        Optional<Book> b = bookRepositor.findById(id);
        return b.orElse(null);
    }

    // POST /api/books
    @PostMapping
    public int save(@RequestBody Book book) {
        return bookRepositor.save(book);
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public int update(@PathVariable int id, @RequestBody Book book) {
        book.setId(id);
        return bookRepositor.update(book);
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public int hardDelete(@PathVariable int id) {
        return bookRepositor.hardDelete(id);
    }
}
