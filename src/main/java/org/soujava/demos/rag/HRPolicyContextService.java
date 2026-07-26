package org.soujava.demos.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.soujava.demos.rag.dto.HRPolicyContextRequest;
import org.soujava.demos.rag.dto.HRPolicyContextResponse;

import java.util.logging.Logger;

@ApplicationScoped
public class HRPolicyContextService {

    private static final Logger LOGGER = Logger.getLogger(HRPolicyContextService.class.getName());
    @Inject
    private EmbeddingModel embeddingModel;

    @Inject
    private EmbeddingStore<TextSegment> vectorDb;

    public HRPolicyContextResponse add(HRPolicyContextRequest request) {
        LOGGER.info("Adding HR policy context to the knowledge base: " + request.context());
        Document document = Document.from(request.context());

        LOGGER.fine("Embedding incoming HR policy context to check for duplicates");
        var documentEmbedding = embeddingModel
                .embed(document.text())
                .content();

        var result = vectorDb.search(
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(documentEmbedding)
                        .maxResults(1)
                        .minScore(0.95)
                        .build()
        );

        if (!result.matches().isEmpty()) {
            LOGGER.info("Similar HR policy context already exists; skipping ingestion: " + request.context());
            return new HRPolicyContextResponse(       false,"Similar HR policy context already exists.");
        }

        LOGGER.fine("No similar context found; ingesting the new HR policy context: " + request.context());
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(100, 10))
                .embeddingModel(embeddingModel)
                .embeddingStore(vectorDb)
                .build();

        ingestor.ingest(document);

        LOGGER.info("HR policy context was added to the knowledge base: " + request.context());
        return new HRPolicyContextResponse(   true,"The HR policy context was added to the knowledge base.");
    }
}