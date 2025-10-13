package com.ai.books.assistant.services.messages.async.prompts;

import com.ai.books.assistant.entities.Chat;
import com.ai.books.assistant.entities.UserPrompt;
import com.ai.books.assistant.repositories.UserPromptRepository;
import com.ai.books.assistant.services.storage.ICloudService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PromptAsyncService implements IPromptAsyncService {

    private final UserPromptRepository userPromptRepository;
    private final ICloudService cloudService;

    public PromptAsyncService(UserPromptRepository userPromptRepository, ICloudService cloudService) {
        this.userPromptRepository = userPromptRepository;
        this.cloudService = cloudService;
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
                fileLink = cloudService.uploadFile(file, objectName);
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
