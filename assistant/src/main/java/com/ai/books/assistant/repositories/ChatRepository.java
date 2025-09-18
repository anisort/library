package com.ai.books.assistant.repositories;

import com.ai.books.assistant.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByUserId(Long userId);
    Optional<Chat> findByUserIdAndId(Long userId, Long id);
}
