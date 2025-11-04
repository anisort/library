package com.ai.books.assistant.services.assistant;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AssistantService {
    ChatResponse getAssistantResponse(String prompt, Long chatId);
    ChatResponse getAssistantResponseMultimodal(String prompt, Long chatId, MultipartFile file);
}
