package com.ibb.library.service;

import com.ibb.library.dto.request.BorrowAdminRequest;
import com.ibb.library.dto.request.BorrowRequest;
import com.ibb.library.dto.response.BorrowingResponse;

import java.util.List;

public interface BorrowingService {
    BorrowingResponse borrowAsCurrentUser(BorrowRequest req);
    BorrowingResponse borrowAsAdmin(BorrowAdminRequest req);
    BorrowingResponse returnBook(Long borrowingId);
    List<BorrowingResponse> myActiveBorrowings();
    List<BorrowingResponse> activeBorrowingsByUser(Long userId); // ADMIN
}
