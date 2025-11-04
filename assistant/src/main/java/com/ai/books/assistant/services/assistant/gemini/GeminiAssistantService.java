package com.ai.books.assistant.services.assistant.gemini;

import com.ai.books.assistant.services.assistant.AssistantService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@Service
public class GeminiAssistantService implements AssistantService {

    private final VertexAiGeminiChatModel chatModel;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    public GeminiAssistantService(VertexAiGeminiChatModel chatModel, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
    }

    @Override
    public ChatResponse getAssistantResponse(String prompt, Long chatId) {

        String systemMessage = """
            You are a Book Assistant. You help users by answering questions about books
            using the information you retrieve from the knowledge base.
        
            The retrieved content for each book may already include:
            - Title, Author, Description
            - Reading link (e.g., link, url, readUrl, openUrl, source)
            - Cover image link
        
            Guidelines:
            - Use this information naturally in your answers.
            - If the user asks where to read or find a book, extract and return the available direct link(s) from the content itself.
            - If several matching books are found, list up to 3 in the format: "Title — <link>".
            - If no reading link is found, say briefly: "Sorry, I couldn’t find a link to read this book."
            - Do not mention that you are using retrieved data — answer directly and conversationally.
            - If the question is unrelated to books, respond briefly: "Sorry, I can only answer questions about books."
            """;

        return ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .advisors( a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .system(systemMessage)
                .user(prompt)
                .call()
                .chatResponse();
    }

    @Override
    public ChatResponse getAssistantResponseMultimodal(String prompt, Long chatId, MultipartFile file) {

        String multimodalSystemMessage = """
            You are a Multimodal Book Assistant.
            The user sends you a text prompt and an attached image or file.
        
            Your task:
            - Carefully analyze both the text and the file (image, cover, or document).
            - Based on them, create a single *text-only* prompt that captures what the user wants to know or do.
            - The resulting text should sound natural and self-contained — it will later be sent to another assistant that only works with text.
            - Do not include file descriptions like "I see a picture of..." — just express the user’s intent in text form.
            - Keep it concise and informative, preserving the user’s question meaning and any important visual details.
        
            Example:
            User text: "Do you have this book?"
            User file: image of "Alice in Wonderland"
            You output: "Find if the system has the book 'Alice in Wonderland'."
            """;

        String contentType = file.getContentType();
        MimeType mimeType = (contentType != null)
                ? MimeTypeUtils.parseMimeType(contentType)
                : MimeTypeUtils.APPLICATION_OCTET_STREAM;

        Resource resource = file.getResource();

        ChatResponse ragPrompt = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build()
                .prompt()
                .system(multimodalSystemMessage)
                .user(u -> u.text(prompt)
                        .media(mimeType, resource))
                .call()
                .chatResponse();

        return getAssistantResponse(Objects.requireNonNull(ragPrompt).getResult().getOutput().getText(), chatId);
    }

}
