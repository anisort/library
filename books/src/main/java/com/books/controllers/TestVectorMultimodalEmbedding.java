package com.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.DocumentEmbeddingRequest;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vertexai.embedding.multimodal.VertexAiMultimodalEmbeddingModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestVectorMultimodalEmbedding {

    private final VertexAiMultimodalEmbeddingModel vertexAiMultimodalEmbeddingModel;
    private final JdbcTemplate jdbcTemplate;

    public TestVectorMultimodalEmbedding(VertexAiMultimodalEmbeddingModel vertexAiMultimodalEmbeddingModel, JdbcTemplate jdbcTemplate) {
        this.vertexAiMultimodalEmbeddingModel = vertexAiMultimodalEmbeddingModel;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("multimodal")
    public float[] getBooks() {
        Media imageMedial = new Media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("/test.png"));
        Document documentText = new Document("Explain me what do you see on this picture?", Map.of("type", "text"));
        Document documentImage = new Document(imageMedial, Map.of("type", "image"));
        DocumentEmbeddingRequest embeddingRequest = new DocumentEmbeddingRequest(List.of(documentText, documentImage));
        EmbeddingResponse embeddingResponse = vertexAiMultimodalEmbeddingModel.call(embeddingRequest);
        float[] vector = embeddingResponse.getResult().getOutput();
        saveEmbedding(
                UUID.randomUUID(),
                "Test multimodal embedding",
                Map.of("source", "test.png", "type", "multimodal"),
                vector
        );
        return vector;
    }

    public void saveEmbedding(UUID id, String content, Map<String, Object> metadata, float[] vector) {
        String sql = "INSERT INTO vector_store (id, content, metadata, embedding) VALUES (?, ?, ?::jsonb, ?)";
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
