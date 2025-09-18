package com.ai.books.assistant.services.assistant.gemini;

import com.ai.books.assistant.services.assistant.IAssistantService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.stereotype.Service;

@Service
public class GeminiAssistantService implements IAssistantService {

    private final VertexAiGeminiChatModel chatModel;
    private final VectorStore vectorStore;

    public GeminiAssistantService(VertexAiGeminiChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Override
    public ChatResponse getAssistantResponse(String prompt) {
        String systemMessage = """
        You are a Book Assistant. You can answer questions about books
        using both the retrieved content and the metadata attached to each book.
        
        Rules:
        - Metadata may include fields such as id, title, author, coverLink, link, url, source, etc.
        - Use metadata naturally in your answers when relevant.
        - If the user asks where to read or find a book, extract and return the direct link(s) strictly from metadata fields:
          link, url, source, readUrl, openUrl (in this order of priority).
        - If multiple candidates are retrieved, list up to 3: "Title — <link>".
        - If no metadata contains a link, say briefly: "Sorry, I couldn't find a link for this book in the metadata."
        - Do not say "based on the context"; just answer directly.
        - If the question is unrelated to the books or metadata, reply briefly:
          "Sorry, I can only answer questions about the books."
        """;

        return ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .system(systemMessage)
                .user(prompt)
                .call()
                .chatResponse();
    }

}
