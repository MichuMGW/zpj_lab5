package com.example.library.library.repository;

import com.example.library.library.entity.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReaderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReaderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reader> readerRowMapper = (rs, rowNum) -> {
        Reader reader = new Reader();
        reader.setId(rs.getInt("id"));
        reader.setName(rs.getString("name"));
        reader.setEmail(rs.getString("email"));
        reader.setActive(rs.getBoolean("active"));
        return reader;
    };

    public int save(Reader reader) {
        String sql = "INSERT INTO Reader (name, email, active) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, reader.getName(), reader.getEmail(), reader.isActive());
    }

    public int update(Reader reader) {
        String sql = "UPDATE Reader SET name = ?, email = ?, active = ? WHERE id = ?";
        return jdbcTemplate.update(sql, reader.getName(), reader.getEmail(), reader.isActive(), reader.getId());
    }

    public List<Reader> findAllActive() {
        String sql = "SELECT id, name, email, active FROM Reader WHERE active = true";
        return jdbcTemplate.query(sql, readerRowMapper);
    }

    public List<Reader> findAll() {
        String sql = "SELECT id, name, email, active FROM Reader";
        return jdbcTemplate.query(sql, readerRowMapper);
    }

    public Optional<Reader> findById(int id) {
        String sql = "SELECT id, name, email, active FROM Reader WHERE id = ?";
        List<Reader> readers = jdbcTemplate.query(sql, readerRowMapper, id);
        return readers.stream().findFirst();
    }

    public int softDelete(int id) {
        String sql = "UPDATE Reader SET active = false WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int hardDelete(int id) {
        String sql = "DELETE FROM Reader WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
