package org.soujava.demos.rag.producer;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.soujava.demos.rag.HRPolicyAgent;

/**
 * Binds the RAG pipeline (chat model + retriever) to the {@link HRPolicyAgent}.
 */
@ApplicationScoped
class HRPolicyAgentProducer {

    @Produces
    @ApplicationScoped
    HRPolicyAgent createAgent(ChatModel chatModel, ContentRetriever retriever) {
        return AiServices.builder(HRPolicyAgent.class)
                .chatModel(chatModel)
                .contentRetriever(retriever)
                .build();
    }
}
