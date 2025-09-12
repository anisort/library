package com.books.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.aiplatform.v1.PredictionServiceSettings;
import org.springframework.ai.vertexai.embedding.VertexAiEmbeddingConnectionDetails;
import org.springframework.ai.vertexai.embedding.text.VertexAiTextEmbeddingModel;
import org.springframework.ai.vertexai.embedding.text.VertexAiTextEmbeddingOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class VertexAiEmbeddingConfig {

    @Value( "${vertex.ai.embedding.api.key.path}")
    private String apiKeyPath;

    @Value( "${spring.ai.vertex.ai.embedding.project-id}")
    private String projectId;

    @Value( "${spring.ai.vertex.ai.embedding.location}")
    private String location;

    @Value( "${spring.ai.vertex.ai.embedding.text.options.model}")
    private String model;

    @Bean
    public VertexAiTextEmbeddingOptions vertexAiTextEmbeddingOptions() {
        return VertexAiTextEmbeddingOptions.builder()
                .model(model)
                .build();
    }

    @Bean
    public VertexAiEmbeddingConnectionDetails vertexAiEmbeddingConnectionDetails() throws IOException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource(apiKeyPath).getInputStream())
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        credentials.refreshIfExpired();

        return VertexAiEmbeddingConnectionDetails.builder()
                        .projectId(projectId)
                        .location(location)
                        .predictionServiceSettings(
                                PredictionServiceSettings.newBuilder()
                                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                                        .build()
                        )
                        .build();
    }

    @Bean
    public VertexAiTextEmbeddingModel vertexAiTextEmbeddingModel(VertexAiEmbeddingConnectionDetails connectionDetails, VertexAiTextEmbeddingOptions options) {
        return new VertexAiTextEmbeddingModel(connectionDetails, options);
    }

}
