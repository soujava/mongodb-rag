package org.soujava.demos.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.util.TypeLiteral;

public class RagMain {

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {

            // 0. Fetch dependencies from CDI
            EmbeddingModel embeddingModel = container.select(EmbeddingModel.class).get();
            EmbeddingStore<TextSegment> vectorDb = container.select(new TypeLiteral<EmbeddingStore<TextSegment>>() {
            }).get();
            HRPolicyAgent agent = container.select(HRPolicyAgent.class).get();

            // PHASE 1: DATA INGESTION
            System.out.println("Ingesting HR Policies into Vector DB...");

            Document document = Document.from(
                    "Company Policy Update 2026: Remote work is permitted on Tuesdays and Thursdays. " +
                            "The annual hardware stipend has been increased to $1,500. " +
                            "Core hours are 10:00 AM to 3:00 PM EST."
            );

            var documentEmbedding = embeddingModel.embed(document.text()).content();
            var result =
                    vectorDb.search(
                            EmbeddingSearchRequest.builder()
                                    .queryEmbedding(documentEmbedding)
                                    .maxResults(5)
                                    .build());

            System.out.println(result.matches().size());

            if (result.matches().isEmpty()) {
                System.out.println("No existing embeddings found for the document. Proceeding with ingestion.");
                // Chunk the document and store the embeddings
                EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                        .documentSplitter(DocumentSplitters.recursive(100, 10)) // 100 tokens, 10 token overlap
                        .embeddingModel(embeddingModel)
                        .embeddingStore(vectorDb)
                        .build();

                ingestor.ingest(document);
            } else {
                System.out.println("Document already ingested. Skipping ingestion step.");
            }

            // PHASE 2: RETRIEVAL & GENERATION
            System.out.println("User: What is my hardware stipend limit?");

            // The framework automatically takes this String, embeds it, queries the DB,
            // appends the fact to the prompt, and asks GPT-4 to generate the answer.
            String response = agent.ask("What is my hardware stipend limit?");

            System.out.println("Agent: " + response);
        }
    }
}
