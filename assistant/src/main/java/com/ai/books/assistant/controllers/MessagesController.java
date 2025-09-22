package com.ai.books.assistant.controllers;

import com.ai.books.assistant.dto.MessageDto;
import com.ai.books.assistant.services.messages.IMessagesService;
import com.ai.books.assistant.services.messages.MessagesService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    private final IMessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping("{chatId}")
    public List<MessageDto> getMessages(@PathVariable Long chatId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return messagesService.getMessages(chatId, userId);
    }

    @PostMapping("{chatId}")
    public MessageDto sendMessage(@PathVariable Long chatId, @RequestBody Map<String, String> prompt, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return messagesService.sendMessage(prompt.get("prompt"), chatId, userId);
    }
}
