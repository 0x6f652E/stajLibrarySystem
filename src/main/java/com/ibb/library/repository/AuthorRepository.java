package com.ibb.library.repository;

import com.ibb.library.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Page<Author> findByFullNameContainingIgnoreCase(String q, Pageable pageable);
    boolean existsByFullNameIgnoreCase(String fullName); // (isteğe bağlı) benzersizlik kontrolü
}
