package com.ibb.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record BookCreateRequest(
        @NotBlank String title,
        @NotNull Long bookshelfId,
        Set<Long> authorIds
) {}
