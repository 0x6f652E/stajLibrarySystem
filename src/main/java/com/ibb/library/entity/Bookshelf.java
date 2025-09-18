package com.ibb.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/** Kitaplık: Bir kütüphaneye bağlıdır ve birden çok kitabı barındırır. */
@Entity
@Table(name = "bookshelves")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bookshelf extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @OneToMany(mappedBy = "bookshelf", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Book> books = new ArrayList<>();

    /** çift yön yönetimi */
    public void addBook(Book book) {
        books.add(book);
        book.setBookshelf(this);
    }
    public void removeBook(Book book) {
        books.remove(book);
        book.setBookshelf(null);
    }
}
