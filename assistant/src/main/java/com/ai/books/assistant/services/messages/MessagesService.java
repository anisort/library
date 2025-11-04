package com.ai.books.assistant.services.messages;

import com.ai.books.assistant.dto.MessageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MessagesService {
    CompletableFuture<MessageDto> sendMessage(String prompt, Long chatId, Long userId);
    CompletableFuture<MessageDto> sendMessageFile(String prompt, MultipartFile file, Long chatId, Long userId);
    List<MessageDto> getMessages(Long chatId, Long userId);
}
