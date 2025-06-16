package com.books.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GcsConfig {

    @Bean
    public Storage storage() throws IOException {
        try (InputStream credentialsStream = getClass().getClassLoader()
                .getResourceAsStream("gcp/libproject-462709-5bb0c6f6bf50.json")) {

            if (credentialsStream == null) {
                throw new IllegalArgumentException("Credential file not found in classpath");
            }

            return StorageOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(credentialsStream))
                    .build()
                    .getService();
        }
    }

}

