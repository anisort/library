package com.ai.books.assistant.controllers;

import com.ai.books.assistant.dto.ChatDto;
import com.ai.books.assistant.services.chat.ChatsService;
import com.ai.books.assistant.services.chat.IChatsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chats")
public class ChatsController {

    private final IChatsService chatsService;

    public ChatsController(ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    @GetMapping
    public List<ChatDto> getChats(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return chatsService.getChats(userId);
    }

    @PostMapping
    public ChatDto createChat(@RequestBody Map<String, String> title, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return chatsService.createChat(userId, title.get("title"));
    }

    @PatchMapping("/{chatId}")
    public ChatDto changeChatTitle(@PathVariable Long chatId, @RequestBody Map<String, String> title, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return chatsService.changeChatTitle(chatId, userId, title.get("title"));
    }

    @DeleteMapping("/{chatId}")
    public void deleteChat(@PathVariable Long chatId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        chatsService.deleteChat(chatId, userId);
    }
}
