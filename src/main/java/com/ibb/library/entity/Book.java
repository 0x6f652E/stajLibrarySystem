package com.ibb.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Kitap: Bir kitaplığa aittir, birden çok yazarı olabilir (N:N).
 */
@Entity
@Table(name = "books")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 180)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookshelf_id")
    private Bookshelf bookshelf;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Builder.Default
    private Set<Author> authors = new HashSet<>();

    /** Çok kopyalı stok için toplam nüsha (NULL ise servis tarafında 1 kabul edilir) */
    @Column(name = "total_copies")
    @Builder.Default
    private Integer totalCopies = 1;

    /** Yazarla çift yönlü ilişkiyi kurma/koparma yardımcıları */
    public void addAuthor(Author author) {
        authors.add(author);
        author.getBooks().add(this);
    }
    public void removeAuthor(Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    /** Silmeden önce N:N ilişkileri güvenle kopar */
    @PreRemove
    private void preRemove() {
        var copy = new HashSet<>(authors);
        for (var a : copy) {
            a.getBooks().remove(this);
        }
        authors.clear();
    }
}
