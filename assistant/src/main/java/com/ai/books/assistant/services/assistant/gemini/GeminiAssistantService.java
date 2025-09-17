package com.ai.books.assistant.services.assistant.gemini;

import com.ai.books.assistant.dto.AssistantResponseDto;
import com.ai.books.assistant.entities.AssistantResponse;
import com.ai.books.assistant.entities.UserPrompt;
import com.ai.books.assistant.repositories.AssistantResponseRepository;
import com.ai.books.assistant.repositories.UserPromptRepository;
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
    private final UserPromptRepository userPromptRepository;
    private final AssistantResponseRepository assistantResponseRepository;

    public GeminiAssistantService(VertexAiGeminiChatModel chatModel, VectorStore vectorStore, UserPromptRepository userPromptRepository, AssistantResponseRepository assistantResponseRepository) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.userPromptRepository = userPromptRepository;
        this.assistantResponseRepository = assistantResponseRepository;
    }

    @Override
    public AssistantResponse getAssistantResponse(String prompt, Long userId) {
        String systemMessage = "You are a helpful assistant";

        UserPrompt userPrompt = saveUserPrompt(prompt, userId);

        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .system(systemMessage)
                .user(userPrompt.getPrompt())
                .call()
                .chatResponse();

        return saveAssistantResponse(response, userPrompt);
    }

    private UserPrompt saveUserPrompt(String prompt, Long userId) {
        UserPrompt userPrompt = new UserPrompt();
        userPrompt.setPrompt(prompt);
        userPrompt.setUserId(userId);
        return userPromptRepository.save(userPrompt);
    }

    private AssistantResponse saveAssistantResponse(ChatResponse response, UserPrompt userPrompt) {
        AssistantResponse assistantResponse = new AssistantResponse();
        assistantResponse.setResponse(Objects.requireNonNull(response).getResult().getOutput().getText());
        assistantResponse.setPromptTokens(response.getMetadata().getUsage().getPromptTokens());
        assistantResponse.setCompletionTokens(response.getMetadata().getUsage().getCompletionTokens());
        assistantResponse.setTotalTokens(response.getMetadata().getUsage().getTotalTokens());
        assistantResponse.setUserPrompt(userPrompt);
        return assistantResponseRepository.save(assistantResponse);
    }

}
