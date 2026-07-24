package org.soujava.demos.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class HRPolicyLoader {

    private static final Logger LOGGER = Logger.getLogger(HRPolicyLoader.class.getName());

    @Inject
    EmbeddingModel embeddingModel;

    @Inject
    EmbeddingStore<TextSegment> vectorDb;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        LOGGER.info("Checking HR policy data in Vector DB...");

        Document document = Document.from(
                "Company Policy Update 2026: " +
                        "Remote work is permitted on Tuesdays and Thursdays. " +
                        "The annual hardware stipend has been increased to $1,500. " +
                        "Core hours are 10:00 AM to 3:00 PM EST."
        );

        var documentEmbedding = embeddingModel.embed(document.text()).content();

        var result = vectorDb.search(
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(documentEmbedding)
                        .maxResults(5)
                        .build()
        );

        LOGGER.info("Matches found: " + result.matches().size());

        if (result.matches().isEmpty()) {
            LOGGER.info("No existing embeddings found. Proceeding with ingestion.");

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(DocumentSplitters.recursive(100, 10))
                    .embeddingModel(embeddingModel)
                    .embeddingStore(vectorDb)
                    .build();

            ingestor.ingest(document);

            LOGGER.info("HR policy document ingested successfully.");
        } else {
            LOGGER.info("Document already ingested. Skipping ingestion step.");
        }
    }
}