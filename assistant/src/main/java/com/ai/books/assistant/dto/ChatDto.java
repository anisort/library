package com.ai.books.assistant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ChatDto {
    private Long id;
    private String title;
    private Instant createdOn;
}
