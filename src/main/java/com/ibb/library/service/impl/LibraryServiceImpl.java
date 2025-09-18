package com.ibb.library.service.impl;

import com.ibb.library.dto.request.LibraryCreateRequest;
import com.ibb.library.dto.request.LibraryUpdateRequest;
import com.ibb.library.dto.response.LibraryResponse;
import com.ibb.library.entity.Library;
import com.ibb.library.exception.ResourceNotFoundException;
import com.ibb.library.mapper.LibraryMapper;
import com.ibb.library.repository.LibraryRepository;
import com.ibb.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository repo;
    private final LibraryMapper mapper;

    @Override
    public LibraryResponse create(LibraryCreateRequest req) {
        Library e = mapper.toEntity(req);
        return mapper.toDto(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibraryResponse> list() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LibraryResponse get(Long id) {
        Library e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kütüphane bulunamadı: " + id));
        return mapper.toDto(e);
    }

    @Override
    public LibraryResponse update(Long id, LibraryUpdateRequest req) {
        Library e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kütüphane bulunamadı: " + id));
        e.setName(req.name());
        return mapper.toDto(e);
    }

    @Override
    public void delete(Long id) {
        Library e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kütüphane bulunamadı: " + id));
        repo.delete(e);
    }
}
