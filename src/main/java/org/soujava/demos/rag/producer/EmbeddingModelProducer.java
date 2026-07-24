package org.soujava.demos.rag.producer;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Produces the {@link EmbeddingModel} used to turn text into vectors.
 */
@ApplicationScoped
class EmbeddingModelProducer {

    @Inject
    @ConfigProperty(name = "dev.langchain4j.cdi.plugin.chat-model.config.api-key")
    private String apiKey;

    @Inject
    @ConfigProperty(name = "rag.embedding.model-name", defaultValue = "text-embedding-3-small")
    private String modelName;

    @Produces
    @ApplicationScoped
    EmbeddingModel createEmbeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }
}
