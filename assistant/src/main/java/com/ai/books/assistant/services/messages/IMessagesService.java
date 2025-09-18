package com.ai.books.assistant.services.messages;

import com.ai.books.assistant.dto.MessageDto;

import java.util.List;

public interface IMessagesService {
    MessageDto sendMessage(String prompt, Long chatId, Long userId);
    List<MessageDto> getMessages(Long chatId, Long userId);
    void deleteMessage(Long userPromptId, Long userId);
}
