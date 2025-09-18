package com.ai.books.assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageDto {
    private UserPromptDto userPrompt;
    private AssistantResponseDto assistantResponse;
}
