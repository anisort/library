package com.ai.books.assistant.repositories;

import com.ai.books.assistant.entities.AssistantResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistantResponseRepository extends JpaRepository<AssistantResponse, Long> {
}
