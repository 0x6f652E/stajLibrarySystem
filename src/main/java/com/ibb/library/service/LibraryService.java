package com.ibb.library.service;

import com.ibb.library.dto.request.LibraryCreateRequest;
import com.ibb.library.dto.request.LibraryUpdateRequest;
import com.ibb.library.dto.response.LibraryResponse;
import java.util.List;

public interface LibraryService {
    LibraryResponse create(LibraryCreateRequest req);
    List<LibraryResponse> list();
    LibraryResponse get(Long id);
    LibraryResponse update(Long id, LibraryUpdateRequest req); // <-- UpdateRequest
    void delete(Long id);
}
