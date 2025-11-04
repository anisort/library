package com.books.utils.seeder.services;

import com.books.entities.Book;
import com.books.repositories.BooksRepository;
import com.books.services.embeddings.VectorStoreService;
import com.books.utils.seeder.dto.GutenBookDto;
import com.books.utils.seeder.dto.GutendexResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class BooksImportServiceImpl implements BookImportService {

    @Value("${api.seeding.url}")
    private String seedingUrl;

    private final RestTemplate restTemplate;
    private final BooksRepository booksRepository;
    private final VectorStoreService<Book> vectorService;

    @Autowired
    public BooksImportServiceImpl(RestTemplate restTemplate, BooksRepository booksRepository, VectorStoreService<Book> vectorService) {
        this.restTemplate = restTemplate;
        this.booksRepository = booksRepository;
        this.vectorService = vectorService;
    }

    @Override
    public void importBooks() {
        String url = seedingUrl;
        int pagesProcessed = 0;
        final int MAX_PAGES = 3;

        while (url != null && pagesProcessed < MAX_PAGES) {

            ResponseEntity<GutendexResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {}
            );

            GutendexResponseDto data = response.getBody();
            if (data == null) break;

            List<Book> books = new ArrayList<>();

            for (GutenBookDto gBook : data.getResults()) {
                if (gBook.getAuthors().isEmpty() || gBook.getSummaries().isEmpty()) continue;

                String link = gBook.getFormats().get("text/html");
                if (link == null) continue;

                String coverLink = gBook.getFormats().get("image/jpeg");

                Book book = new Book();
                book.setTitle(gBook.getTitle());
                book.setAuthor(gBook.getAuthors().getFirst().getName());
                book.setSummary(gBook.getSummaries().getFirst());
                book.setLink(link);
                book.setCoverLink(coverLink);

                book = booksRepository.save(book);
                books.add(book);
            }

            vectorService.addToVectorStore(books);

            System.out.printf("Page %d processed (%d books). Waiting 1 min...%n", pagesProcessed + 1, books.size());

            try {
                System.out.println("Waiting 1 minute before next page...");
                Thread.sleep(60000);
            } catch (InterruptedException ignored) {}

            url = data.getNext();
            pagesProcessed++;
        }
    }

}


