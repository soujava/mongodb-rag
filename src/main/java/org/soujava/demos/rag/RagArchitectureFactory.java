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

    @Produces
    @ApplicationScoped
    public EmbeddingModel createEmbeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName("text-embedding-3-small")
                .build();
    }

    @Produces
    @ApplicationScoped
    public EmbeddingStore<TextSegment> createVectorDatabase() {
        MongoClient client = MongoClients.create(mongodbURL);
        MongoDatabase database = client.getDatabase("ai-patterns");
        String databaseName = "rag_app";
        String collectionName = "embeddings";
        String indexName = "embedding";
        Long maxResultRatio = 10L;
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions();
        Bson filter = null;
        Set<String> metadataFields = new HashSet<>();
        IndexMapping indexMapping = new IndexMapping(1536, metadataFields);
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
                .maxResults(3) // Fetch the top 3 most relevant chunks
                .minScore(0.7) // Strict boundary: Ignore low-confidence matches
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