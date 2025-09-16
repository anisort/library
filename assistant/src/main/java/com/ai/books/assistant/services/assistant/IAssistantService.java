package com.ai.books.assistant.services.assistant;

import com.ai.books.assistant.dto.AssistantResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface IAssistantService {
    AssistantResponseDto getAssistantResponse(String prompt);
}
