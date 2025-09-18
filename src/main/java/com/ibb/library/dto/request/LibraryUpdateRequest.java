package com.ibb.library.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LibraryUpdateRequest(
        @NotBlank String name
) {}
