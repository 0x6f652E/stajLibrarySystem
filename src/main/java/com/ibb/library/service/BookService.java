// src/main/java/com/ibb/library/service/BookService.java
package com.ibb.library.service;

import com.ibb.library.dto.request.BookCreateRequest;
import com.ibb.library.dto.request.BookUpdateRequest;
import com.ibb.library.dto.response.BookResponse;
import org.springframework.data.domain.Page;         // <-- EKLE
import org.springframework.data.domain.Pageable;    // <-- EKLE
import java.util.List;

public interface BookService {
    BookResponse create(BookCreateRequest req);
    List<BookResponse> list();
    BookResponse get(Long id);
    BookResponse update(Long id, BookUpdateRequest req);
    void delete(Long id);
    List<BookResponse> searchByAuthor(String authorName);

    // <-- YENİ
    Page<BookResponse> search(String title, String author, String library, Pageable pageable);
}
