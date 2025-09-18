package com.ibb.library.controller;

import com.ibb.library.dto.request.BookshelfCreateRequest;
import com.ibb.library.dto.request.BookshelfUpdateRequest;
import com.ibb.library.dto.response.BookshelfResponse;
import com.ibb.library.service.BookshelfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelves")
@RequiredArgsConstructor
public class BookshelfController {

    private final BookshelfService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public BookshelfResponse create(@Valid @RequestBody BookshelfCreateRequest req) {
        return service.create(req);
    }

    @GetMapping("/by-library/{libraryId}")
    public List<BookshelfResponse> listByLibrary(@PathVariable Long libraryId) {
        return service.listByLibrary(libraryId);
    }

    @GetMapping("/{id}")
    public BookshelfResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BookshelfResponse update(@PathVariable Long id, @Valid @RequestBody BookshelfUpdateRequest req) {
        return service.update(id, req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
