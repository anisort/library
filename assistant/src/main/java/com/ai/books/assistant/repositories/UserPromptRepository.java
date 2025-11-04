package com.ai.books.assistant.repositories;

import com.ai.books.assistant.entities.UserPrompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPromptRepository extends JpaRepository <UserPrompt, Long>{
    List<UserPrompt> findAllByChatId(Long chatId);
}
