package com.ai.books.assistant.config.database;

import com.pgvector.PGvector;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class PgVectorConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.vector")
    public DataSourceProperties vectorDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource vectorDataSource(@Qualifier("vectorDataSourceProperties") DataSourceProperties vectorDataSourceProperties) {
        return vectorDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate vectorJdbcTemplate(@Qualifier("vectorDataSource") DataSource vectorDataSource) {
        try (Connection conn = vectorDataSource.getConnection()) {
            PGvector.registerTypes(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register PGVector types", e);
        }
        return new JdbcTemplate(vectorDataSource);
    }

    @Bean
    public PgVectorStore pgVectorStore(@Qualifier("vectorJdbcTemplate") JdbcTemplate vectorJdbcTemplate,
                                       EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(vectorJdbcTemplate, embeddingModel)
                .schemaName("public")
                .vectorTableName("vector_store")
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .build();
    }
}