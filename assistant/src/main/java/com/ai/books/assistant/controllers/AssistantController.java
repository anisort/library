package com.ai.books.assistant.controllers;

import com.ai.books.assistant.dto.AssistantResponseDto;
import com.ai.books.assistant.entities.AssistantResponse;
import com.ai.books.assistant.services.assistant.IAssistantService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    private final IAssistantService assistantService;

    public AssistantController(IAssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @RequestMapping
    public AssistantResponse getAssistantResponse(@RequestBody Map<String, String> payload, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return assistantService.getAssistantResponse(payload.get("prompt"), userId);
    }
}
