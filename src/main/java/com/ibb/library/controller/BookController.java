package com.ibb.library.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibb.library.dto.request.BookCreateRequest;
import com.ibb.library.dto.request.BookUpdateRequest;
import com.ibb.library.dto.response.BookResponse;
import com.ibb.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public BookResponse create(@Valid @RequestBody BookCreateRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<BookResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    // 2) ESKİ "yalnızca yazar" ucu → PATH'i değiştiriyoruz
    @Deprecated
    @GetMapping("/search-by-author")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<BookResponse> searchByAuthor(@RequestParam String author) {
        return service.searchByAuthor(author);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookUpdateRequest req) {
        return service.update(id, req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Page<BookResponse> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String library,
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "title") // varsayılan sıralama: title ASC
            }) Pageable pageable
    ) {
        return service.search(title, author, library, pageable);
    }

}
