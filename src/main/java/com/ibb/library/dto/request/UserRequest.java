package com.ibb.library.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank @Size(min = 2, max = 80) String firstName,
        @NotBlank @Size(min = 2, max = 80) String lastName,
        @Email @NotBlank String email,
        // Sadece create/update'ta gelir; response'ta asla dönmüyoruz.
        @Size(min = 6) String password,
        // "ADMIN" veya "USER"
        @NotBlank String role
) {}
