package com.sourav.trasactionextractor.config;


import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionExtAiConfig {
    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.chat-model}")
    private String chatModel;

    @Value("${openai.embedding-model}")
    private String embedModel;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    @Bean
    public OpenAiChatModel chatModel(){
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModel)
                .build();
    }

    @Bean
    public OpenAiEmbeddingModel embeddingModel(){
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(embedModel)
                .build();
    }

    @Bean
    public PgVectorEmbeddingStore embeddingStore(){
        return PgVectorEmbeddingStore.builder()
                .host("localhost")
                .port(5432)
                .database("textr")
                .user(dbUser)
                .password(dbPass)
                .table("transactions")
                .dimension(1536)
                .build();
    }
}
