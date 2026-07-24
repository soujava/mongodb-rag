package org.soujava.demos.rag;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
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

@ApplicationScoped
public class RagArchitectureFactory {

    @Inject
    @ConfigProperty(name = "jnosql.mongodb.url")
    private String mongodbURL;


    @Inject
    @ConfigProperty(name = "dev.langchain4j.cdi.plugin.chat-model.config.api-key")
    private String apiKey;

    @Inject
    @ConfigProperty(name = "rag.embedding.model-name", defaultValue = "text-embedding-3-small")
    private String embeddingModelName;

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

    @Inject
    @ConfigProperty(name = "rag.retriever.max-results", defaultValue = "3")
    private int maxResults;

    @Inject
    @ConfigProperty(name = "rag.retriever.min-score", defaultValue = "0.7")
    private double minScore;

    @Produces
    @ApplicationScoped
    public EmbeddingModel createEmbeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(embeddingModelName)
                .build();
    }

    @Produces
    @ApplicationScoped
    public EmbeddingStore<TextSegment> createVectorDatabase() {
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

    @Produces
    @ApplicationScoped
    public HRPolicyAgent createAgent(ChatModel chatModel, ContentRetriever retriever) {
        // Binds the RAG pipeline to the Agent
        return AiServices.builder(HRPolicyAgent.class)
                .chatModel(chatModel)
                .contentRetriever(retriever)
                .build();
    }
}