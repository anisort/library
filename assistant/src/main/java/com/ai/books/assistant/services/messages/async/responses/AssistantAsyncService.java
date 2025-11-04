package com.ai.books.assistant.services.messages.async.responses;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface AssistantAsyncService {
    CompletableFuture<ChatResponse> getResponseAsync(String prompt, Long chatId, MultipartFile file);
}
