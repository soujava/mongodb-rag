package org.soujava.demos.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Produces the {@link ContentRetriever} that searches the vector store for a query.
 */
@ApplicationScoped
public class ContentRetrieverProducer {

    @Inject
    @ConfigProperty(name = "rag.retriever.max-results", defaultValue = "3")
    private int maxResults;

    @Inject
    @ConfigProperty(name = "rag.retriever.min-score", defaultValue = "0.7")
    private double minScore;

    @Produces
    @ApplicationScoped
    public ContentRetriever createRetriever(EmbeddingStore<TextSegment> store, EmbeddingModel model) {
        // The architectural bridge that searches the DB based on the query vector
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .embeddingModel(model)
                .maxResults(maxResults) // Fetch the top N most relevant chunks
                .minScore(minScore) // Strict boundary: Ignore low-confidence matches
                .build();
    }
}
