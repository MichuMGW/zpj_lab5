package com.example.library.library.controller;

import com.example.library.library.entity.Book;
import com.example.library.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /api/books
    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public Book findById(@PathVariable int id) {
        Optional<Book> b = bookService.findById(id);
        return b.orElse(null);
    }

    // POST /api/books
    @PostMapping
    public int save(@RequestBody Book book) {
        return bookService.save(book);
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public int update(@PathVariable int id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public int hardDelete(@PathVariable int id) {
        return bookService.hardDelete(id);
    }
}
