// com/ibb/library/dto/request/BorrowAdminRequest.java
package com.ibb.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BorrowAdminRequest(
        @NotNull Long userId,
        @NotNull Long bookId,
        @Min(1) Integer days
) {}
