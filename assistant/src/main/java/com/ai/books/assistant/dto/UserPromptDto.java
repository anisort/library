package com.ai.books.assistant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserPromptDto {
    private Long id;
    private String prompt;
    private Instant createdOn;
}
