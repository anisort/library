package com.ai.books.assistant.controllers;

import com.ai.books.assistant.dto.AssistantResponseDto;
import com.ai.books.assistant.services.assistant.IAssistantService;
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
    public AssistantResponseDto getAssistantResponse(@RequestBody Map<String, String> payload) {
        return assistantService.getAssistantResponse(payload.get("prompt"));
    }
}
