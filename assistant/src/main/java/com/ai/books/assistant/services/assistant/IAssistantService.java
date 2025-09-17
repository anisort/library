package com.ai.books.assistant.services.assistant;

import com.ai.books.assistant.dto.AssistantResponseDto;
import com.ai.books.assistant.entities.AssistantResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IAssistantService {
    AssistantResponse getAssistantResponse(String prompt, Long userId);
}
