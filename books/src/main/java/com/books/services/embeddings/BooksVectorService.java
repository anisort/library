package com.books.services.embeddings;

import com.books.entities.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.DocumentEmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vertexai.embedding.multimodal.VertexAiMultimodalEmbeddingModel;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BooksVectorService implements IVectorStoreService<Book> {

    private final VectorStore vectorStore;
    private final VertexAiMultimodalEmbeddingModel vertexAiMultimodalEmbeddingModel;
    private final JdbcTemplate jdbcTemplate;

    public BooksVectorService(
            VectorStore vectorStore,
            VertexAiMultimodalEmbeddingModel vertexAiMultimodalEmbeddingModel,
            JdbcTemplate jdbcTemplate
    ) {
        this.vectorStore = vectorStore;
        this.vertexAiMultimodalEmbeddingModel = vertexAiMultimodalEmbeddingModel;
        this.jdbcTemplate = jdbcTemplate;
    }

    // !!!
    // java.lang.RuntimeException: com.google.api.gax.rpc.InvalidArgumentException: io.grpc.StatusRuntimeException: INVALID_ARGUMENT: Multimodal embedding failed with the following error: Text field must be smaller than 1024 characters.
    @Override
    public void addToVectorStore(Book book) {
        try {
            UUID uuid = getUuid(book.getId());
            String coverLink = book.getCoverLink();

            String content = String.format(
                    "Title: %s\nAuthor: %s\nSummary: %s\nRead online: %s\nLink to cover: %s",
                    book.getTitle(),
                    book.getAuthor(),
                    book.getSummary(),
                    book.getLink(),
                    coverLink
            );

            Media media = new Media(detectMimeTypeFromUrl(coverLink), new UrlResource(coverLink));

            Document textDocument = new Document(
                    content,
                    Map.of("type", "text")
            );

            Document mediaDocument = new Document(
                    media,
                    Map.of("type", "media")
            );

            DocumentEmbeddingRequest request = new DocumentEmbeddingRequest(List.of(textDocument, mediaDocument));
            EmbeddingResponse response = vertexAiMultimodalEmbeddingModel.call(request);

            saveEmbedding(
                    uuid,
                    content,
                    Map.of("book_id", book.getId()),
                    response.getResult().getOutput()
            );

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid coverLink URL: " + book.getCoverLink(), e);
        }
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

    private MimeType detectMimeTypeFromUrl(String urlString) {
        try {
            URL url = URI.create(urlString).toURL();
            URLConnection connection = url.openConnection();
            String contentType = connection.getContentType();
            return (contentType != null)
                    ? MimeTypeUtils.parseMimeType(contentType)
                    : MimeTypeUtils.APPLICATION_OCTET_STREAM;

        } catch (Exception e) {
            return MimeTypeUtils.APPLICATION_OCTET_STREAM;
        }
    }

    private void saveEmbedding(UUID id, String content, Map<String, Object> metadata, float[] vector) {
        String sql = "INSERT INTO vector_store (id, content, metadata, embedding) VALUES (?::uuid, ?::text, ?::jsonb, ?::vector)";
        try {
            jdbcTemplate.update(sql,
                    id,
                    content,
                    new ObjectMapper().writeValueAsString(metadata),
                    vector);
        } catch (Exception e) {
            throw new RuntimeException("Error saving embedding to pgvector", e);
        }
    }
}
