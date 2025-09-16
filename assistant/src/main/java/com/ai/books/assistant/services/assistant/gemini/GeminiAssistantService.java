package com.ai.books.assistant.services.assistant.gemini;

import com.ai.books.assistant.dto.AssistantResponseDto;
import com.ai.books.assistant.services.assistant.IAssistantService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GeminiAssistantService implements IAssistantService {

    private final VertexAiGeminiChatModel chatModel;
    private final VectorStore vectorStore;

    public GeminiAssistantService(VertexAiGeminiChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Override
    public AssistantResponseDto getAssistantResponse(String prompt) {
        String systemMessage = "You are a helpful assistant";
        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .system(systemMessage)
                .user(prompt)
                .call()
                .chatResponse();

        AssistantResponseDto assistantResponseDto = new AssistantResponseDto();
        assistantResponseDto.setResponse(Objects.requireNonNull(response).getResult().getOutput().getText());
        assistantResponseDto.setPromptTokens(response.getMetadata().getUsage().getPromptTokens());
        assistantResponseDto.setCompletionTokens(response.getMetadata().getUsage().getCompletionTokens());
        assistantResponseDto.setTotalTokens(response.getMetadata().getUsage().getTotalTokens());

        return assistantResponseDto;
    }

}
