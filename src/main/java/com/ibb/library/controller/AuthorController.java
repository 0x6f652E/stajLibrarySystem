package com.ibb.library.controller;

import com.ibb.library.dto.request.AuthorCreateRequest;
import com.ibb.library.dto.request.AuthorUpdateRequest;
import com.ibb.library.dto.response.AuthorResponse;
import com.ibb.library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public AuthorResponse create(@Valid @RequestBody AuthorCreateRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<AuthorResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public AuthorResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public AuthorResponse update(@PathVariable Long id, @Valid @RequestBody AuthorUpdateRequest req) {
        return service.update(id, req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
