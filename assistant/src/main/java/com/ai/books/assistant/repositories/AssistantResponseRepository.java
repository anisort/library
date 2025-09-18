package com.ai.books.assistant.repositories;

import com.ai.books.assistant.entities.AssistantResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssistantResponseRepository extends JpaRepository<AssistantResponse, Long> {
    Optional<AssistantResponse> findByUserPromptId(Long userPromptId);
}
