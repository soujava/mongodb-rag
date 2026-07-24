package org.soujava.demos.rag;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.CreateCollectionOptions;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.mongodb.IndexMapping;
import dev.langchain4j.store.embedding.mongodb.MongoDbEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Produces the MongoDB-backed {@link EmbeddingStore} that persists and searches vectors.
 */
@ApplicationScoped
public class VectorStoreProducer {

    @Inject
    @ConfigProperty(name = "jnosql.mongodb.url")
    private String mongodbURL;

    @Inject
    @ConfigProperty(name = "rag.embedding.dimension", defaultValue = "1536")
    private int embeddingDimension;

    @Inject
    @ConfigProperty(name = "rag.mongodb.database", defaultValue = "rag_app")
    private String databaseName;

    @Inject
    @ConfigProperty(name = "rag.mongodb.collection", defaultValue = "embeddings")
    private String collectionName;

    @Inject
    @ConfigProperty(name = "rag.mongodb.index", defaultValue = "embedding")
    private String indexName;

    @Inject
    @ConfigProperty(name = "rag.mongodb.max-result-ratio", defaultValue = "10")
    private long maxResultRatio;

    @Produces
    @ApplicationScoped
    public EmbeddingStore<TextSegment> createVectorStore() {
        MongoClient client = MongoClients.create(mongodbURL);
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions();
        Bson filter = null;
        Set<String> metadataFields = new HashSet<>();
        IndexMapping indexMapping = new IndexMapping(embeddingDimension, metadataFields);
        Boolean createIndex = true;
        return new MongoDbEmbeddingStore(
                client,
                databaseName,
                collectionName,
                indexName,
                maxResultRatio,
                createCollectionOptions,
                filter,
                indexMapping,
                createIndex
        );
    }
}
