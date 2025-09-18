package com.ibb.library.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthorCreateRequest(
        @NotBlank String fullName
) {}
