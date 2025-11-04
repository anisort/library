package com.ai.books.assistant.controllers;

import com.ai.books.assistant.dto.MessageDto;
import com.ai.books.assistant.services.messages.MessagesService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping("{chatId}")
    public List<MessageDto> getMessages(@PathVariable Long chatId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return messagesService.getMessages(chatId, userId);
    }

    @PostMapping("{chatId}")
    public CompletableFuture<MessageDto> sendMessage(@PathVariable Long chatId, @RequestBody Map<String, String> prompt, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return messagesService.sendMessage(prompt.get("prompt"), chatId, userId);
    }

    @PostMapping(value = "/file/{chatId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<MessageDto> sendMessageFile(@PathVariable Long chatId, @RequestPart("prompt") String prompt, @RequestPart(value = "file") MultipartFile file, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return messagesService.sendMessageFile(prompt, file, chatId, userId);
    }
}
