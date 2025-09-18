package com.ibb.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookshelfCreateRequest(
        @NotBlank String name,
        @NotNull Long libraryId
) {}
