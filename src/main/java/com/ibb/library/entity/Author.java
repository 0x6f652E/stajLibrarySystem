package com.ibb.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Yazar: Birden çok kitabı olabilir (N:N).
 */
@Entity
@Table(name = "authors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Author extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true, length = 160)
    private String fullName;

    @ManyToMany(mappedBy = "authors")
    @Builder.Default
    private Set<Book> books = new HashSet<>();

    /** Kitapla çift yönlü ilişkiyi kurma/koparma yardımcıları */
    public void addBook(Book book) {
        books.add(book);
        book.getAuthors().add(this);
    }
    public void removeBook(Book book) {
        books.remove(book);
        book.getAuthors().remove(this);
    }

    /** Silmeden önce N:N ilişkileri güvenle kopar */
    @PreRemove
    private void preRemove() {
        var copy = new HashSet<>(books);
        for (var b : copy) {
            b.getAuthors().remove(this);
        }
        books.clear();
    }
}
