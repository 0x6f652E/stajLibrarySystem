package com.ibb.library.controller;

import com.ibb.library.dto.request.LibraryCreateRequest;
import com.ibb.library.dto.request.LibraryUpdateRequest;
import com.ibb.library.dto.response.LibraryResponse;
import com.ibb.library.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public LibraryResponse create(@Valid @RequestBody LibraryCreateRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<LibraryResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public LibraryResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public LibraryResponse update(@PathVariable Long id, @Valid @RequestBody LibraryUpdateRequest req) {
        return service.update(id, req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
