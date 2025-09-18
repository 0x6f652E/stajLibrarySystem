package com.ibb.library.service;

import com.ibb.library.dto.request.AuthorCreateRequest;
import com.ibb.library.dto.request.AuthorUpdateRequest;
import com.ibb.library.dto.response.AuthorResponse;
import java.util.List;

public interface AuthorService {
    AuthorResponse create(AuthorCreateRequest req);
    List<AuthorResponse> list();
    AuthorResponse get(Long id);
    AuthorResponse update(Long id, AuthorUpdateRequest req); // <-- UpdateRequest
    void delete(Long id);
}
