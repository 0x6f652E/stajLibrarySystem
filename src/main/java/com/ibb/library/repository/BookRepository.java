// src/main/java/com/ibb/library/repository/BookRepository.java
package com.ibb.library.repository;

import com.ibb.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- EKLE

import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> { // <-- EKLE

    List<Book> findByBookshelfId(Long bookshelfId);
    List<Book> findByAuthorsFullNameContainingIgnoreCase(String fullName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdForUpdate(@Param("id") Long id);
}
