package com.ibb.library.repository;

import com.ibb.library.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    long countByBookIdAndReturnedAtIsNull(Long bookId);

    boolean existsByUserIdAndBookIdAndReturnedAtIsNull(Long userId, Long bookId);

    List<Borrowing> findByUserIdAndReturnedAtIsNull(Long userId);
}
