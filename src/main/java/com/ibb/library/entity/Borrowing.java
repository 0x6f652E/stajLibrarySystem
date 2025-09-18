package com.ibb.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "borrowings")
@Getter @Setter
public class Borrowing extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // ödünç alan kullanıcı

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book; // ödünç verilen kitap

    @Column(nullable = false)
    private Instant borrowedAt;

    @Column(nullable = false)
    private Instant dueAt;

    @Column
    private Instant returnedAt; // iade edilince dolar

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowingStatus status;
}
