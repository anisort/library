package com.ai.books.assistant.services.chat;

import com.ai.books.assistant.dto.ChatDto;

import java.util.List;

public interface IChatsService {

    List<ChatDto> getChats(Long userId);
    ChatDto createChat(Long userId);
    ChatDto changeChatTitle(Long chatId, Long userId, String title);
    void deleteChat(Long chatId, Long userId);

}
