package com.example.library.library.service;

import com.example.library.library.entity.Book;
import com.example.library.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    public int save(Book book) {
        return bookRepository.save(book);
    }

    public int update(int id, Book book) {
        book.setId(id);
        return bookRepository.update(book);
    }

    public int hardDelete(int id) {
        return bookRepository.hardDelete(id);
    }
}