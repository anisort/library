package com.ai.books.assistant.services.chat;

import com.ai.books.assistant.dto.ChatDto;
import com.ai.books.assistant.entities.Chat;
import com.ai.books.assistant.repositories.ChatRepository;
import com.ai.books.assistant.utils.converters.ChatsConverter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChatsService implements IChatsService {

    private final ChatRepository chatRepository;

    public ChatsService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public List<ChatDto> getChats(Long userId) {
        return chatRepository.findAllByUserId(userId)
                .stream()
                .map(ChatsConverter::convertChatToDto)
                .toList();
    }

    @Override
    public ChatDto createChat(Long userId, String title) {
        Chat chat = new Chat();
        chat.setUserId(userId);
        chat.setTitle(title);
        return ChatsConverter.convertChatToDto(chatRepository.save(chat));
    }

    @Override
    public ChatDto changeChatTitle(Long chatId, Long userId, String title) {
        Chat chat = chatRepository.findByUserIdAndId(userId, chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found in user's history"));
        chat.setTitle(title);
        return ChatsConverter.convertChatToDto(chatRepository.save(chat));
    }

    @Override
    public void deleteChat(Long chatId, Long userId) {
        Chat chat = chatRepository.findByUserIdAndId(userId, chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found in user's history"));
        chatRepository.delete(chat);
    }
}
