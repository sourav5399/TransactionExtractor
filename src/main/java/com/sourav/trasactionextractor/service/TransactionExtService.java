package com.sourav.trasactionextractor.service;

import com.sourav.trasactionextractor.intf.TransactionExtBot;
import com.sourav.trasactionextractor.model.TransactionResponse;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentLoader;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Service
public class TransactionExtService {
    private final OpenAiChatModel chatModel;
    private final OpenAiEmbeddingModel embeddingModel;
    private final PgVectorEmbeddingStore embeddingStore;

    public TransactionExtService(OpenAiChatModel chatModel, OpenAiEmbeddingModel embeddingModel,
                                 PgVectorEmbeddingStore embeddingStore){
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    public void ingestPdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Document document = new ApachePdfBoxDocumentParser().parse(inputStream);
            document.metadata().put("fileName", file.getOriginalFilename());

            // Ingest into vector store
            EmbeddingStoreIngestor esi = EmbeddingStoreIngestor.builder()
                    .embeddingStore(embeddingStore)
                    .embeddingModel(embeddingModel)
                    .build();
            esi.ingest(document);
        }
    }

    public TransactionResponse getRecentTransactions() {
        EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                //.maxResults(15)
                .build();

        // Dynamically build the AI agent with our context retriever
        TransactionExtBot bot = AiServices.builder(TransactionExtBot.class)
                .chatModel(chatModel)
                .contentRetriever(retriever)
                .build();

        String today = LocalDate.now().toString();
        String promptTemplate = String.format(
                "Today's date is %s. Examine the provided context fragments and identify all financial transactions " +
                        "that took place strictly within the last 3 days. Output them sequentially from oldest to newest. Output should be a list of transactions with from, amount, to, time. " +
                        "If no transactions match, clearly state that none were found.",
                today
        );

        return bot.analyze(promptTemplate);
    }

}
