package com.ibb.library.config;

import com.ibb.library.entity.*;
import com.ibb.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

/**
 * ER modelini hızlı doğrulamak için örnek veri.
 */
@Configuration
@RequiredArgsConstructor
public class DomainSeeder implements CommandLineRunner {

    private final LibraryRepository libraryRepo;
    private final BookshelfRepository shelfRepo;
    private final AuthorRepository authorRepo;
    private final BookRepository bookRepo;

    @Override
    @Transactional
    public void run(String... args) {
        if (libraryRepo.count() > 0) return; // sadece boşken çalışsın

        var merkez = Library.builder().name("Merkez Kütüphane").build();
        libraryRepo.save(merkez);

        var roman = Bookshelf.builder().name("Roman").library(merkez).build();
        var bilim = Bookshelf.builder().name("Bilim").library(merkez).build();
        shelfRepo.save(roman);
        shelfRepo.save(bilim);

        var pamuk = Author.builder().fullName("Orhan Pamuk").build();
        var harari = Author.builder().fullName("Yuval Noah Harari").build();
        authorRepo.save(pamuk);
        authorRepo.save(harari);

        var kirmizi = Book.builder().title("Kırmızı Saçlı Kadın").bookshelf(roman).build();
        kirmizi.addAuthor(pamuk);
        bookRepo.save(kirmizi);

        var sapiens = Book.builder().title("Sapiens").bookshelf(bilim).build();
        sapiens.addAuthor(harari);
        bookRepo.save(sapiens);
    }
}
