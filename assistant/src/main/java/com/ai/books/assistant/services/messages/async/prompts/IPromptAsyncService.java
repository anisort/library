package com.ai.books.assistant.services.messages.async.prompts;

import com.ai.books.assistant.entities.Chat;
import com.ai.books.assistant.entities.UserPrompt;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface IPromptAsyncService {
    CompletableFuture<UserPrompt> savePromptAsync(String prompt, MultipartFile file, Chat chat);
}
