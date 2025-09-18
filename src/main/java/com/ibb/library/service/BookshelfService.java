package com.ibb.library.service;

import com.ibb.library.dto.request.BookshelfCreateRequest;
import com.ibb.library.dto.request.BookshelfUpdateRequest;
import com.ibb.library.dto.response.BookshelfResponse;
import java.util.List;

public interface BookshelfService {
    BookshelfResponse create(BookshelfCreateRequest req);
    List<BookshelfResponse> listByLibrary(Long libraryId);
    BookshelfResponse get(Long id);
    BookshelfResponse update(Long id, BookshelfUpdateRequest req); // <-- UpdateRequest
    void delete(Long id);
}
