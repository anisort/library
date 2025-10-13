package com.ai.books.assistant.utils.converters;

import com.ai.books.assistant.dto.UserPromptDto;
import com.ai.books.assistant.entities.UserPrompt;

public class PromptsConverter {

    public static UserPromptDto convertPromptToDto(UserPrompt prompt) {
        UserPromptDto promptDto = new UserPromptDto();
        promptDto.setId(prompt.getId());
        promptDto.setPrompt(prompt.getPrompt());
        promptDto.setFileLink(prompt.getFileLink());
        promptDto.setCreatedOn(prompt.getCreatedOn());
        return promptDto;
    }
}
