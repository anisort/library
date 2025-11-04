package com.books.services.embeddings;

import com.books.entities.Book;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BooksVectorService implements VectorStoreService<Book> {

    private final VectorStore vectorStore;

    public BooksVectorService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void addToVectorStore(List<Book> books) {
        List<Document> documents = new ArrayList<>();
        for (Book book : books) {
            UUID uuid = getUuid(book.getId());
            String content = String.format(
                    "Title: %s\nAuthor: %s\nSummary: %s\nRead online: %s\nLink to cover: %s",
                    book.getTitle(),
                    book.getAuthor(),
                    book.getSummary(),
                    book.getLink(),
                    book.getCoverLink()
            );
            Document document = new Document(uuid.toString(), content, Map.of("book_id", book.getId()));
            documents.add(document);
        }
        vectorStore.add(documents);
    }

    @Override
    public void updateInVectorStore(Book book) {
        deleteFromVectorStore(book.getId());
        addToVectorStore(List.of(book));
    }

    @Override
    public void deleteFromVectorStore(Long id) {
        UUID uuid = getUuid(id);
        vectorStore.delete(List.of(String.valueOf(uuid)));
    }

    private UUID getUuid(Long id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes());
    }
}
