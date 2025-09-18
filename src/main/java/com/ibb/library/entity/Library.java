package com.ibb.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/** Kütüphane: Birden çok kitaplığa sahiptir. */
@Entity
@Table(name = "libraries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Library extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Bookshelf> bookshelves = new ArrayList<>();

    /** çift yön yönetimi */
    public void addBookshelf(Bookshelf shelf) {
        bookshelves.add(shelf);
        shelf.setLibrary(this);
    }
    public void removeBookshelf(Bookshelf shelf) {
        bookshelves.remove(shelf);
        shelf.setLibrary(null);
    }
}
