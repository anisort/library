package com.ai.books.assistant.services.messages.async.prompts;

import com.ai.books.assistant.entities.Chat;
import com.ai.books.assistant.entities.UserPrompt;
import com.ai.books.assistant.repositories.UserPromptRepository;
import com.ai.books.assistant.services.storage.StorageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PromptAsyncServiceImpl implements PromptAsyncService {

    private final UserPromptRepository userPromptRepository;
    private final StorageService storageService;

    public PromptAsyncServiceImpl(UserPromptRepository userPromptRepository, StorageService storageService) {
        this.userPromptRepository = userPromptRepository;
        this.storageService = storageService;
    }

    @Async
    @Override
    public CompletableFuture<UserPrompt> savePromptAsync(String prompt, MultipartFile file, Chat chat) {
        try {
            String fileLink = null;
            if (file != null) {
                String originalName = (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank())
                        ? file.getOriginalFilename()
                        : "upload";
                String objectName = UUID.randomUUID() + "-" + originalName;
                fileLink = storageService.uploadFile(file, objectName);
            }

            UserPrompt up = new UserPrompt();
            up.setPrompt(prompt);
            up.setFileLink(fileLink);
            up.setChat(chat);

            return CompletableFuture.completedFuture(userPromptRepository.save(up));
        } catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
