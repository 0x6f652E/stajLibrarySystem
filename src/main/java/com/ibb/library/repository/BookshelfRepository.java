package com.ibb.library.repository;

import com.ibb.library.entity.Bookshelf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookshelfRepository extends JpaRepository<Bookshelf, Long> {
    Page<Bookshelf> findByNameContainingIgnoreCase(String q, Pageable pageable);
    List<Bookshelf> findByLibraryId(Long libraryId);
}
