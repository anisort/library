package com.ai.books.assistant.utils.converters;

import com.ai.books.assistant.dto.ChatDto;
import com.ai.books.assistant.entities.Chat;

public class ChatsConverter {

    public static ChatDto convertChatToDto(Chat chat) {
        ChatDto chatDto = new ChatDto();
        chatDto.setId(chat.getId());
        chatDto.setTitle(chat.getTitle());
        chatDto.setCreatedOn(chat.getCreatedOn());
        return chatDto;
    }
}
