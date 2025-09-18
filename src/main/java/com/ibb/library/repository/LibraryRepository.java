package com.ibb.library.repository;

import com.ibb.library.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    Page<Library> findByNameContainingIgnoreCase(String q, Pageable pageable);
    boolean existsByNameIgnoreCase(String name); // (isteğe bağlı) create/update öncesi 409 için
}
