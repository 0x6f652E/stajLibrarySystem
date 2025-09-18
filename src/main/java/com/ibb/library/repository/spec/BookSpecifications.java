// src/main/java/com/ibb/library/repository/spec/BookSpecifications.java
package com.ibb.library.repository.spec;

import com.ibb.library.entity.Book;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecifications {

    private BookSpecifications() {}

    public static Specification<Book> titleContains(String q) {
        if (q == null || q.isBlank()) return null;
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(cb.lower(root.get("title")), like);
        };
    }

    // N:N join -> Author
    public static Specification<Book> authorNameContains(String q) {
        if (q == null || q.isBlank()) return null;
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            query.distinct(true);
            var authors = root.join("authors", JoinType.LEFT);
            return cb.like(cb.lower(authors.get("fullName")), like);
        };
    }

    // Book -> Bookshelf -> Library
    public static Specification<Book> libraryNameContains(String q) {
        if (q == null || q.isBlank()) return null;
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            query.distinct(true);
            var shelf = root.join("bookshelf", JoinType.LEFT);
            var library = shelf.join("library", JoinType.LEFT);
            return cb.like(cb.lower(library.get("name")), like);
        };
    }
}
