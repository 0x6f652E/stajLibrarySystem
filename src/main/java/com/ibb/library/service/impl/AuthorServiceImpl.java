package com.ibb.library.service.impl;

import com.ibb.library.dto.request.AuthorCreateRequest;
import com.ibb.library.dto.request.AuthorUpdateRequest;
import com.ibb.library.dto.response.AuthorResponse;
import com.ibb.library.entity.Author;
import com.ibb.library.exception.ResourceNotFoundException;
import com.ibb.library.mapper.AuthorMapper;
import com.ibb.library.repository.AuthorRepository;
import com.ibb.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repo;
    private final AuthorMapper mapper;

    @Override
    public AuthorResponse create(AuthorCreateRequest req) {
        Author a = mapper.toEntity(req);
        return mapper.toDto(repo.save(a));
    }

    @Override @Transactional(readOnly = true)
    public List<AuthorResponse> list() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override @Transactional(readOnly = true)
    public AuthorResponse get(Long id) {
        Author a = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yazar bulunamadı: " + id));
        return mapper.toDto(a);
    }

    @Override
    public AuthorResponse update(Long id, AuthorUpdateRequest req) {
        Author a = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yazar bulunamadı: " + id));
        a.setFullName(req.fullName());
        return mapper.toDto(a);
    }

    @Override
    public void delete(Long id) {
        Author a = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yazar bulunamadı: " + id));
        repo.delete(a); // @PreRemove tetiklenir
    }
}
