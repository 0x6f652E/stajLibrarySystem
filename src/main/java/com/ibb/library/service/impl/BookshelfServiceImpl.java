package com.ibb.library.service.impl;

import com.ibb.library.dto.request.BookshelfCreateRequest;
import com.ibb.library.dto.request.BookshelfUpdateRequest;
import com.ibb.library.dto.response.BookshelfResponse;
import com.ibb.library.entity.Bookshelf;
import com.ibb.library.entity.Library;
import com.ibb.library.exception.ResourceNotFoundException;
import com.ibb.library.mapper.BookshelfMapper;
import com.ibb.library.repository.BookshelfRepository;
import com.ibb.library.repository.LibraryRepository;
import com.ibb.library.service.BookshelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookshelfServiceImpl implements BookshelfService {

    private final BookshelfRepository repo;
    private final LibraryRepository libraryRepo;
    private final BookshelfMapper mapper;

    @Override
    public BookshelfResponse create(BookshelfCreateRequest req) {
        Library lib = libraryRepo.findById(req.libraryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kütüphane bulunamadı: " + req.libraryId()));
        Bookshelf shelf = mapper.toEntity(req);
        shelf.setLibrary(lib);
        return mapper.toDto(repo.save(shelf));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookshelfResponse> listByLibrary(Long libraryId) {
        return repo.findByLibraryId(libraryId).stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookshelfResponse get(Long id) {
        var shelf = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitaplık bulunamadı: " + id));
        return mapper.toDto(shelf);
    }

    @Override
    public BookshelfResponse update(Long id, BookshelfUpdateRequest req) {
        var shelf = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitaplık bulunamadı: " + id));
        var lib = libraryRepo.findById(req.libraryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kütüphane bulunamadı: " + req.libraryId()));
        shelf.setName(req.name());
        shelf.setLibrary(lib);
        return mapper.toDto(shelf);
    }

    @Override
    public void delete(Long id) {
        var shelf = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitaplık bulunamadı: " + id));
        repo.delete(shelf);
    }
}
