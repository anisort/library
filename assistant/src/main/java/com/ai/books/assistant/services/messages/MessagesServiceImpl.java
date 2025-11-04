package com.ai.books.assistant.services.messages;

import com.ai.books.assistant.dto.MessageDto;
import com.ai.books.assistant.entities.AssistantResponse;
import com.ai.books.assistant.entities.Chat;
import com.ai.books.assistant.entities.UserPrompt;
import com.ai.books.assistant.repositories.AssistantResponseRepository;
import com.ai.books.assistant.repositories.ChatRepository;
import com.ai.books.assistant.repositories.UserPromptRepository;
import com.ai.books.assistant.services.messages.async.prompts.PromptAsyncService;
import com.ai.books.assistant.services.messages.async.responses.AssistantAsyncService;
import com.ai.books.assistant.utils.converters.AssistantResponsesConverter;
import com.ai.books.assistant.utils.converters.PromptsConverter;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class MessagesServiceImpl implements MessagesService {

    private final UserPromptRepository userPromptRepository;
    private final AssistantResponseRepository assistantResponseRepository;
    private final ChatRepository chatRepository;
    private final PromptAsyncService promptAsyncService;
    private final AssistantAsyncService assistantAsyncService;

    public MessagesServiceImpl(
            UserPromptRepository userPromptRepository,
            AssistantResponseRepository assistantResponseRepository,
            ChatRepository chatRepository,
            PromptAsyncService promptAsyncService,
            AssistantAsyncService assistantAsyncService

    ) {
        this.userPromptRepository = userPromptRepository;
        this.assistantResponseRepository = assistantResponseRepository;
        this.chatRepository = chatRepository;
        this.promptAsyncService = promptAsyncService;
        this.assistantAsyncService = assistantAsyncService;
    }

    @Override
    public CompletableFuture<MessageDto> sendMessage(String prompt, Long chatId, Long userId) {
        return processMessage(prompt, chatId, userId, null);
    }

    @Override
    public CompletableFuture<MessageDto> sendMessageFile(String prompt, MultipartFile file, Long chatId, Long userId) {
        return processMessage(prompt, chatId, userId, file);
    }

    @Override
    public List<MessageDto> getMessages(Long chatId, Long userId) {
        Chat chat = chatRepository.findByUserIdAndId(userId, chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found in user's history"));
        List<UserPrompt> userPrompts = userPromptRepository.findAllByChatId(chat.getId());
        List<MessageDto> messages = new ArrayList<>();
        for (UserPrompt userPrompt : userPrompts) {
            AssistantResponse assistantResponse = assistantResponseRepository.findByUserPromptId(userPrompt.getId())
                    .orElseGet(() -> {
                        AssistantResponse placeholder = new AssistantResponse();
                        placeholder.setId(null);
                        placeholder.setResponse("Response not found for this prompt");
                        placeholder.setPromptTokens(0);
                        placeholder.setCompletionTokens(0);
                        placeholder.setTotalTokens(0);
                        placeholder.setCreatedOn(Instant.now());
                        placeholder.setUserPrompt(userPrompt);
                        return placeholder;
                    });
            messages.add(new MessageDto(PromptsConverter.convertPromptToDto(userPrompt), AssistantResponsesConverter.convertAssistantResponseToDto(assistantResponse)));
        }
        return messages;
    }

    private CompletableFuture<MessageDto> processMessage(String prompt, Long chatId, Long userId, MultipartFile file) {
        Chat chat = chatRepository.findByUserIdAndId(userId, chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found in user's history"));

        CompletableFuture<UserPrompt> userPromptFuture = promptAsyncService.savePromptAsync(prompt, file, chat);
        CompletableFuture<ChatResponse> responseFuture = assistantAsyncService.getResponseAsync(prompt, chatId, file);

        return CompletableFuture.allOf(userPromptFuture, responseFuture)
            .thenApply(voidVal -> {
                try {
                    UserPrompt userPrompt = userPromptFuture.get();
                    ChatResponse response = responseFuture.get();

                    AssistantResponse assistantResponse = getAssistantResponse(response, userPrompt);
                    assistantResponse = assistantResponseRepository.save(assistantResponse);

                    return new MessageDto(
                            PromptsConverter.convertPromptToDto(userPrompt),
                            AssistantResponsesConverter.convertAssistantResponseToDto(assistantResponse)
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
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
