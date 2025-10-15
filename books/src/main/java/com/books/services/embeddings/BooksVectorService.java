package com.books.services.embeddings;

import com.books.entities.Book;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BooksVectorService implements IVectorStoreService<Book> {

    private final VectorStore vectorStore;

    public BooksVectorService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void addToVectorStore(Book book) {
        UUID uuid = getUuid(book.getId());
        Document document = new Document(
                uuid.toString(),
                book.getSummary(),
                Map.of(
                        "id", book.getId(),
                        "title", book.getTitle(),
                        "author", book.getAuthor(),
                        "coverLink", book.getCoverLink(),
                        "link", book.getLink()
                )
        );
        vectorStore.add(List.of(document));
    }

    @Override
    public void updateInVectorStore(Book book) {
        deleteFromVectorStore(book.getId());
        addToVectorStore(book);
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
