package com.ai.books.assistant.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_prompt")
public class UserPrompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @CreationTimestamp
    private Instant createdOn;

    @OneToOne(mappedBy = "userPrompt", cascade = CascadeType.ALL, orphanRemoval = true)
    private AssistantResponse assistantResponse;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
