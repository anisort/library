package com.ai.books.assistant.services.chat;

import com.ai.books.assistant.dto.ChatDto;

import java.util.List;

public interface ChatsService {

    List<ChatDto> getChats(Long userId);
    ChatDto createChat(Long userId, String title);
    ChatDto changeChatTitle(Long chatId, Long userId, String title);
    void deleteChat(Long chatId, Long userId);

}
