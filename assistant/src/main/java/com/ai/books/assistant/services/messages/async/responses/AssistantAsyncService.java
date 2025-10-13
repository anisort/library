package com.ai.books.assistant.services.messages.async.responses;

import com.ai.books.assistant.services.assistant.IAssistantService;
import com.ai.books.assistant.services.assistant.gemini.GeminiAssistantService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Service
public class AssistantAsyncService implements IAssistantAsyncService {

    private final IAssistantService assistantService;

    public AssistantAsyncService(GeminiAssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @Async
    @Override
    public CompletableFuture<ChatResponse> getResponseAsync(String prompt, Long chatId, MultipartFile file) {
        if (file != null) {
            return CompletableFuture.completedFuture(
                    assistantService.getAssistantResponseMultimodal(prompt, chatId, file)
            );
        } else {
            return CompletableFuture.completedFuture(
                    assistantService.getAssistantResponse(prompt, chatId)
            );
        }
    }
}
