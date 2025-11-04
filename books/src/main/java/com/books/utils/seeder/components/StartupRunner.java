package com.books.utils.seeder.components;

import com.books.repositories.BooksRepository;
import com.books.utils.seeder.services.BooksImportServiceImpl;
import com.books.utils.seeder.services.BookImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final BookImportService booksImportService;
    private final BooksRepository booksRepository;

    public StartupRunner(BookImportService booksImportService, BooksRepository booksRepository) {
        this.booksImportService = booksImportService;
        this.booksRepository = booksRepository;
    }

    @Override
    public void run(String... args) {
        if (booksRepository.count() == 0) {
            booksImportService.importBooks();
            System.out.println("Books imported.");
        } else {
            System.out.println("Books already exist. Skipping import.");
        }
    }
}

