package com.ai.books.assistant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AssistantResponseDto {
    private Long id;
    private String response;
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
    private Instant createdOn;
}
