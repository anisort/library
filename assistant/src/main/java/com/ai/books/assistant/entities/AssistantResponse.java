package com.ai.books.assistant.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "assistant_response")
public class AssistantResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String response;

    private int promptTokens;

    private int completionTokens;

    private int totalTokens;

    @CreationTimestamp
    private Instant createdOn;

    @OneToOne
    @JoinColumn(name = "user_prompt_id", unique = true)
    private UserPrompt userPrompt;


}
