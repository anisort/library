package com.books.utils.seeder.services;

import com.books.entities.Book;
import com.books.repositories.BooksRepository;
import com.books.utils.seeder.dto.GutenBookDto;
import com.books.utils.seeder.dto.GutendexResponseDto;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BooksImportService implements IBookImportService{

    @Value("${api.seeding.url}")
    private String seedingUrl;

    private final RestTemplate restTemplate;
    private final BooksRepository booksRepository;
    private final VectorStore vectorStore;

    @Autowired
    public BooksImportService(RestTemplate restTemplate, BooksRepository booksRepository, VectorStore vectorStore) {
        this.restTemplate = restTemplate;
        this.booksRepository = booksRepository;
        this.vectorStore = vectorStore;
    }

    @Override
    public void importBooks() {
        String url = seedingUrl;
        int pagesProcessed = 0;
        final int MAX_PAGES = 15;

        while (url != null && pagesProcessed < MAX_PAGES) {

            ResponseEntity<GutendexResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {}
            );

            GutendexResponseDto data = response.getBody();
            if (data == null) break;

            List<Document> documents = new ArrayList<>();
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

                UUID uuid = UUID.nameUUIDFromBytes(String.valueOf(book.getId()).getBytes());
                Document document = new Document(uuid.toString(), book.getSummary(), Map.of(
                        "id", book.getId(),
                        "title", book.getTitle(),
                        "author", book.getAuthor(),
                        "coverLink", book.getCoverLink(),
                        "link", book.getLink()
                ));

                documents.add(document);
            }

            vectorStore.add(documents);

            url = data.getNext();
            pagesProcessed++;
        }
    }

}


