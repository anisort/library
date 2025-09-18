package com.ai.books.assistant.services.assistant;

import org.springframework.ai.chat.model.ChatResponse;

public interface IAssistantService {
    ChatResponse getAssistantResponse(String prompt);
}
