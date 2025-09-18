package com.ai.books.assistant.utils.converters;

import com.ai.books.assistant.dto.AssistantResponseDto;
import com.ai.books.assistant.entities.AssistantResponse;

public class AssistantResponsesConverter {

    public static AssistantResponseDto convertAssistantResponseToDto(AssistantResponse assistantResponse) {
        AssistantResponseDto assistantResponseDto = new AssistantResponseDto();
        assistantResponseDto.setResponse(assistantResponse.getResponse());
        assistantResponseDto.setPromptTokens(assistantResponse.getPromptTokens());
        assistantResponseDto.setCompletionTokens(assistantResponse.getCompletionTokens());
        assistantResponseDto.setTotalTokens(assistantResponse.getTotalTokens());
        assistantResponseDto.setCreatedOn(assistantResponse.getCreatedOn());
        return assistantResponseDto;
    }
}
