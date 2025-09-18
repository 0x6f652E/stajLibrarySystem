// com/ibb/library/dto/request/BorrowRequest.java
package com.ibb.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BorrowRequest(
        @NotNull Long bookId,
        @Min(1) Integer days // null ise serviste 14 gün kabul ederiz
) {}
