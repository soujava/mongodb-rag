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

import java.util.logging.Logger;

@ApplicationScoped
public class HRPolicyContextService {

    private static final Logger LOGGER = Logger.getLogger(HRPolicyContextService.class.getName());
    @Inject
    private EmbeddingModel embeddingModel;

    @Inject
    private EmbeddingStore<TextSegment> vectorDb;

    public HRPolicyContextResponse add(HRPolicyContextRequest request) {
        Document document = Document.from(request.context());

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
            return new HRPolicyContextResponse("Similar HR policy context already exists.");
        }

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(100, 10))
                .embeddingModel(embeddingModel)
                .embeddingStore(vectorDb)
                .build();

        ingestor.ingest(document);

        return new HRPolicyContextResponse("The HR policy context was added to the knowledge base.");
    }
}