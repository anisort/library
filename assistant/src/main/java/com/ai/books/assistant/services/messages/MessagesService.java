package com.ai.books.assistant.services.messages;

import com.ai.books.assistant.dto.MessageDto;
import com.ai.books.assistant.entities.AssistantResponse;
import com.ai.books.assistant.entities.Chat;
import com.ai.books.assistant.entities.UserPrompt;
import com.ai.books.assistant.repositories.AssistantResponseRepository;
import com.ai.books.assistant.repositories.ChatRepository;
import com.ai.books.assistant.repositories.UserPromptRepository;
import com.ai.books.assistant.services.assistant.IAssistantService;
import com.ai.books.assistant.services.assistant.gemini.GeminiAssistantService;
import com.ai.books.assistant.utils.converters.AssistantResponsesConverter;
import com.ai.books.assistant.utils.converters.PromptsConverter;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MessagesService implements IMessagesService {

    private final UserPromptRepository userPromptRepository;
    private final AssistantResponseRepository assistantResponseRepository;
    private final ChatRepository chatRepository;
    private final IAssistantService assistantService;

    public MessagesService(
            UserPromptRepository userPromptRepository,
            AssistantResponseRepository assistantResponseRepository,
            ChatRepository chatRepository,
            GeminiAssistantService assistantService) {
        this.userPromptRepository = userPromptRepository;
        this.assistantResponseRepository = assistantResponseRepository;
        this.chatRepository = chatRepository;
        this.assistantService = assistantService;
    }

    @Override
    public MessageDto sendMessage(String prompt, Long chatId, Long userId) {
        Chat chat = chatRepository.findByUserIdAndId(userId, chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found in user's history"));

        UserPrompt userPrompt = new UserPrompt();
        userPrompt.setPrompt(prompt);
        userPrompt.setChat(chat);

        userPrompt = userPromptRepository.save(userPrompt);

        ChatResponse response = assistantService.getAssistantResponse(userPrompt.getPrompt());

        AssistantResponse assistantResponse = getAssistantResponse(response, userPrompt);
        assistantResponse = assistantResponseRepository.save(assistantResponse);

        return new MessageDto(PromptsConverter.convertPromptToDto(userPrompt), AssistantResponsesConverter.convertAssistantResponseToDto(assistantResponse));
    }

    @Override
    public List<MessageDto> getMessages(Long chatId, Long userId) {
        Chat chat = chatRepository.findByUserIdAndId(userId, chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found in user's history"));
        List<UserPrompt> userPrompts = userPromptRepository.findAllByChatId(chat.getId());
        List<MessageDto> messages = new ArrayList<>();
        for (UserPrompt userPrompt : userPrompts) {
            AssistantResponse assistantResponse = assistantResponseRepository.findByUserPromptId(userPrompt.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Response not found for this prompt"));
            messages.add(new MessageDto(PromptsConverter.convertPromptToDto(userPrompt), AssistantResponsesConverter.convertAssistantResponseToDto(assistantResponse)));
        }
        return messages;
    }

    @Override
    public void deleteMessage(Long userPromptId, Long userId) {
        UserPrompt userPrompt = userPromptRepository.findByIdAndChat_UserId(userPromptId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prompt not found in user's history"));
        userPromptRepository.delete(userPrompt);
    }

    private static AssistantResponse getAssistantResponse(ChatResponse response, UserPrompt userPrompt) {
        AssistantResponse assistantResponse = new AssistantResponse();
        assistantResponse.setResponse(Objects.requireNonNull(response).getResult().getOutput().getText());
        assistantResponse.setPromptTokens(response.getMetadata().getUsage().getPromptTokens());
        assistantResponse.setCompletionTokens(response.getMetadata().getUsage().getCompletionTokens());
        assistantResponse.setTotalTokens(response.getMetadata().getUsage().getTotalTokens());
        assistantResponse.setUserPrompt(userPrompt);
        return assistantResponse;
    }
}
