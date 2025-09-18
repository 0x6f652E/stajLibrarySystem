package com.ibb.library.dto.response;

import java.util.List;

public record BookResponse(
        Long id,
        String title,
        Long bookshelfId,
        List<AuthorResponse> authors
) {}
