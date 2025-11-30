package com.example.library.library.repository;

import com.example.library.library.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Book book) {
        String sql = "INSERT INTO Book (id, title, author, year) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear());
    }

    public int update(Book book) {
        String sql = "UPDATE Book SET title = ?, author = ?, year = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getId());
    }

    public List<Book> findAll() {
        String sql = "SELECT id, title, author, year FROM Book";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Book b = new Book();
            b.setId(rs.getInt("id"));
            b.setTitle(rs.getString("title"));
            b.setAuthor(rs.getString("author"));
            b.setYear(rs.getInt("year"));
            return b;
        });
    }

    public Optional<Book> findById(int id) {
        String sql = "SELECT id, title, author, year FROM Book WHERE id = ?";
        List<Book> books = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Book b = new Book();
            b.setId(rs.getInt("id"));
            b.setTitle(rs.getString("title"));
            b.setAuthor(rs.getString("author"));
            b.setYear(rs.getInt("year"));
            return b;
        }, id);
        return books.stream().findFirst();
    }

    public int hardDelete(int id) {
        String sql = "DELETE FROM Book WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
