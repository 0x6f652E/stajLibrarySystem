package com.ibb.library.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import static com.ibb.library.repository.spec.BookSpecifications.*;

import com.ibb.library.dto.request.BookCreateRequest;
import com.ibb.library.dto.request.BookUpdateRequest;
import com.ibb.library.dto.response.BookResponse;
import com.ibb.library.entity.Author;
import com.ibb.library.entity.Book;
import com.ibb.library.entity.Bookshelf;
import com.ibb.library.exception.ResourceNotFoundException;
import com.ibb.library.mapper.BookMapper;
import com.ibb.library.repository.AuthorRepository;
import com.ibb.library.repository.BookRepository;
import com.ibb.library.repository.BookshelfRepository;
import com.ibb.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository repo;
    private final BookshelfRepository shelfRepo;
    private final AuthorRepository authorRepo;
    private final BookMapper mapper;

    @Override
    public BookResponse create(BookCreateRequest req) {
        Bookshelf shelf = shelfRepo.findById(req.bookshelfId())
                .orElseThrow(() -> new ResourceNotFoundException("Kitaplık bulunamadı: " + req.bookshelfId()));

        Book book = mapper.toEntity(req);
        book.setBookshelf(shelf);

        if (req.authorIds() != null && !req.authorIds().isEmpty()) {
            List<Author> authors = authorRepo.findAllById(req.authorIds());
            if (authors.size() != req.authorIds().size()) {
                throw new ResourceNotFoundException("Bazı yazar id'leri bulunamadı");
            }
            authors.forEach(book::addAuthor);
        }
        return mapper.toDto(repo.save(book));
    }

    @Override @Transactional(readOnly = true)
    public List<BookResponse> list() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override @Transactional(readOnly = true)
    public BookResponse get(Long id) {
        Book b = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı: " + id));
        return mapper.toDto(b);
    }

    @Override
    public BookResponse update(Long id, BookUpdateRequest req) {
        Book b = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı: " + id));

        b.setTitle(req.title());

        Bookshelf shelf = shelfRepo.findById(req.bookshelfId())
                .orElseThrow(() -> new ResourceNotFoundException("Kitaplık bulunamadı: " + req.bookshelfId()));
        b.setBookshelf(shelf);

        // Yazar ilişkilerini yeniden kur
        b.getAuthors().forEach(a -> a.getBooks().remove(b));
        b.getAuthors().clear();

        if (req.authorIds() != null && !req.authorIds().isEmpty()) {
            List<Author> authors = authorRepo.findAllById(req.authorIds());
            if (authors.size() != req.authorIds().size()) {
                throw new ResourceNotFoundException("Bazı yazar id'leri bulunamadı");
            }
            authors.forEach(b::addAuthor);
        }
        return mapper.toDto(b);
    }

    @Override
    public void delete(Long id) {
        Book b = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı: " + id));
        repo.delete(b); // @PreRemove tetiklenir
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> search(String title, String author, String library, Pageable pageable) {
        Specification<Book> spec = Specification.where(titleContains(title))
                .and(authorNameContains(author))
                .and(libraryNameContains(library));

        // MapStruct: mapper.toResponse(entity) kullanıyorum.
        // Eğer senin mapper metodun toDto ise -> .map(mapper::toDto) yap.
        return repo.findAll(spec, pageable).map(mapper::toDto);
    }

    @Override @Transactional(readOnly = true)
    public List<BookResponse> searchByAuthor(String authorName) {
        return repo.findByAuthorsFullNameContainingIgnoreCase(authorName)
                .stream().map(mapper::toDto).toList();
    }
}
