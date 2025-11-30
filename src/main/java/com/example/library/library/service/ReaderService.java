package com.example.library.library.service;

import com.example.library.library.entity.Reader;
import com.example.library.library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public int save(Reader reader) {
        return readerRepository.save(reader);
    }

    public Optional<Reader> findById(int id) {
        return readerRepository.findById(id);
    }

    public List<Reader> findAll(){
        return readerRepository.findAll();
    }

    public List<Reader> findAllActive() {
        return readerRepository.findAllActive();
    }

    public int update(int id, Reader readerDetails) {
        Optional<Reader> existingReader = readerRepository.findById(id);
        if (existingReader.isEmpty()) {
            return -1;
        }
        readerDetails.setId(id);
        int result = readerRepository.update(readerDetails);
        return result;
    }

    public int softDelete(int id) {
        return readerRepository.softDelete(id);
    }

    public int hardDelete(int id) {
        return readerRepository.hardDelete(id);
    }
}