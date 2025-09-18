package com.ibb.library.service.impl;

import com.ibb.library.dto.request.BorrowAdminRequest;
import com.ibb.library.dto.request.BorrowRequest;
import com.ibb.library.dto.response.BorrowingResponse;
import com.ibb.library.entity.Book;
import com.ibb.library.entity.Borrowing;
import com.ibb.library.entity.BorrowingStatus;
import com.ibb.library.entity.User;
import com.ibb.library.exception.ResourceNotFoundException;
import com.ibb.library.repository.BookRepository;
import com.ibb.library.repository.BorrowingRepository;
import com.ibb.library.repository.UserRepository;
import com.ibb.library.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // sınıf genelinde write işlemleri için
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    // --- yardımcılar ---
    private static int normalizeDays(Integer days) {
        return (days == null || days < 1) ? 14 : days;
    }

    private static BorrowingResponse toDto(Borrowing b, long activeCount, int totalCopiesNorm) {
        int available = Math.max(0, totalCopiesNorm - (int) activeCount);
        return new BorrowingResponse(
                b.getId(),
                b.getUser().getId(),
                b.getUser().getEmail(),
                b.getBook().getId(),
                b.getBook().getTitle(),
                b.getBorrowedAt(),
                b.getDueAt(),
                b.getReturnedAt(),
                b.getStatus().name(),
                totalCopiesNorm,
                available
        );
    }

    private User requireCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (auth == null) ? null : (String) auth.getPrincipal();
        if (email == null) throw new IllegalStateException("Kimlik doğrulama yok.");
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + email));
    }

    private BorrowingResponse doBorrow(User user, Long bookId, Integer days) {
        // 1) Kitabı kilitleyerek oku (pessimistic write)
        Book book = bookRepo.findByIdForUpdate(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı: " + bookId));

        int total = (book.getTotalCopies() == null) ? 1 : book.getTotalCopies();
        long activeCount = borrowingRepo.countByBookIdAndReturnedAtIsNull(bookId);

        if (activeCount >= total) {
            throw new IllegalStateException("Stokta uygun kopya yok.");
        }

        // aynı kullanıcı aynı kitabı aynı anda ikinci kez almasın
        if (borrowingRepo.existsByUserIdAndBookIdAndReturnedAtIsNull(user.getId(), bookId)) {
            throw new IllegalStateException("Kullanıcıda bu kitabın aktif bir borcu var.");
        }

        // 2) Kayıt oluştur
        Borrowing br = new Borrowing();
        br.setUser(user);
        br.setBook(book);
        br.setBorrowedAt(Instant.now());
        br.setDueAt(Instant.now().plus(normalizeDays(days), ChronoUnit.DAYS));
        br.setStatus(BorrowingStatus.ACTIVE);

        Borrowing saved = borrowingRepo.save(br);

        long newActive = activeCount + 1;
        return toDto(saved, newActive, total);
    }

    @Override
    public BorrowingResponse borrowAsCurrentUser(BorrowRequest req) {
        User current = requireCurrentUser();
        return doBorrow(current, req.bookId(), req.days());
    }

    @Override
    public BorrowingResponse borrowAsAdmin(BorrowAdminRequest req) {
        User u = userRepo.findById(req.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + req.userId()));
        return doBorrow(u, req.bookId(), req.days());
    }

    @Override
    public BorrowingResponse returnBook(Long borrowingId) {
        Borrowing b = borrowingRepo.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Ödünç kaydı bulunamadı: " + borrowingId));

        // sahiplik kontrolü: ADMIN her şeyi yapar; USER sadece kendi borcunu iade edebilir
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        String email = (auth != null) ? (String) auth.getPrincipal() : null;

        if (!isAdmin && (email == null || !b.getUser().getEmail().equals(email))) {
            throw new IllegalStateException("Bu borç size ait değil.");
        }

        if (b.getReturnedAt() != null) {
            throw new IllegalStateException("Kayıt zaten iade edilmiş.");
        }

        b.setReturnedAt(Instant.now());
        b.setStatus(BorrowingStatus.RETURNED);
        Borrowing saved = borrowingRepo.save(b);

        long active = borrowingRepo.countByBookIdAndReturnedAtIsNull(saved.getBook().getId());
        int total = (saved.getBook().getTotalCopies() == null) ? 1 : saved.getBook().getTotalCopies();

        return toDto(saved, active, total);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> myActiveBorrowings() {
        User current = requireCurrentUser();
        List<Borrowing> list = borrowingRepo.findByUserIdAndReturnedAtIsNull(current.getId());
        return list.stream().map(b -> {
            long active = borrowingRepo.countByBookIdAndReturnedAtIsNull(b.getBook().getId());
            int total = (b.getBook().getTotalCopies() == null) ? 1 : b.getBook().getTotalCopies();
            return toDto(b, active, total);
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> activeBorrowingsByUser(Long userId) {
        List<Borrowing> list = borrowingRepo.findByUserIdAndReturnedAtIsNull(userId);
        return list.stream().map(b -> {
            long active = borrowingRepo.countByBookIdAndReturnedAtIsNull(b.getBook().getId());
            int total = (b.getBook().getTotalCopies() == null) ? 1 : b.getBook().getTotalCopies();
            return toDto(b, active, total);
        }).toList();
    }
}
