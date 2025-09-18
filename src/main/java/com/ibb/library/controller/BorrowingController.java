package com.ibb.library.controller;

import com.ibb.library.dto.request.BorrowAdminRequest;
import com.ibb.library.dto.request.BorrowRequest;
import com.ibb.library.dto.response.BorrowingResponse;
import com.ibb.library.service.BorrowingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService service;

    // USER kendi adına; ADMIN de kullanabilir ama genelde admin endpoint'i ayrı:
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public BorrowingResponse borrowForMe(@Valid @RequestBody BorrowRequest req) {
        return service.borrowAsCurrentUser(req);
    }

    // ADMIN başka bir kullanıcı adına ödünç verir
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public BorrowingResponse borrowForUser(@Valid @RequestBody BorrowAdminRequest req) {
        return service.borrowAsAdmin(req);
    }

    // USER kendi borcunu; ADMIN herkesinkini iade edebilir (servis içinde kontrol var)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{id}/return")
    public BorrowingResponse returnBook(@PathVariable Long id) {
        return service.returnBook(id);
    }

    // USER kendi aktif borçları
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/active")
    public List<BorrowingResponse> myActive() {
        return service.myActiveBorrowings();
    }

    // ADMIN belirli bir kullanıcının aktif borçlarını görür
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active/by-user/{userId}")
    public List<BorrowingResponse> activeByUser(@PathVariable Long userId) {
        return service.activeBorrowingsByUser(userId);
    }
}
