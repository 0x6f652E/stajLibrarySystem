// com/ibb/library/dto/response/BorrowingResponse.java
package com.ibb.library.dto.response;

import java.time.Instant;

public record BorrowingResponse(
        Long id,
        Long userId,
        String userEmail,
        Long bookId,
        String bookTitle,
        Instant borrowedAt,
        Instant dueAt,
        Instant returnedAt,
        String status,
        Integer totalCopies,
        Integer availableCopies // (total - aktif) anlık hesap
) {}
