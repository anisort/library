package com.ai.books.assistant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistantResponseDto {
    private String response;
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
}
