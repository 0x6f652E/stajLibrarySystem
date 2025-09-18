package com.ibb.library.dto.response;

public record BookshelfResponse(
        Long id,
        String name,
        Long libraryId
) {}
