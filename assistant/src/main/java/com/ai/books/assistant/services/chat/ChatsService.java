package com.ai.books.assistant.services.chat;

import com.ai.books.assistant.dto.ChatDto;
import com.ai.books.assistant.entities.Chat;
import com.ai.books.assistant.entities.UserPrompt;
import com.ai.books.assistant.repositories.ChatRepository;
import com.ai.books.assistant.repositories.UserPromptRepository;
import com.ai.books.assistant.services.storage.ICloudService;
import com.ai.books.assistant.utils.converters.ChatsConverter;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChatsService implements IChatsService {

    private final ChatRepository chatRepository;
    private final UserPromptRepository userPromptRepository;
    private final ChatMemory chatMemory;
    private final ICloudService gcsService;

    public ChatsService(ChatRepository chatRepository, UserPromptRepository userPromptRepository, ChatMemory chatMemory, ICloudService gcsService) {
        this.chatRepository = chatRepository;
        this.userPromptRepository = userPromptRepository;
        this.chatMemory = chatMemory;
        this.gcsService = gcsService;
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

        chatMemory.clear(String.valueOf(chatId));

        List<UserPrompt> prompts = userPromptRepository.findAllByChatId(chatId);
        for (UserPrompt prompt : prompts) {
            String fileLink = prompt.getFileLink();
            if (fileLink != null) {
                gcsService.deleteFile(fileLink);
            }
        }

        chatRepository.delete(chat);
    }
}
