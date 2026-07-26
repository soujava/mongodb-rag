package org.soujava.demos.rag;


import dev.langchain4j.cdi.spi.RegisterAIService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * LangChain4j AI service that answers HR policy questions.
 *
 * <p>Registered declaratively via {@link RegisterAIService}: the CDI extension
 * builds the service proxy and wires the default {@code ChatModel} together with
 * the default {@code ContentRetriever} (the RAG bridge to the vector store), so
 * no manual producer is needed. The bean is application scoped so it can also be
 * resolved outside a request context (for example in {@code RagMain}).
 */
@RegisterAIService(scope = ApplicationScoped.class, contentRetrieverName = "#default")
public interface HRPolicyAgent {

    @SystemMessage("""
            You are an assistant responsible for answering questions about
            the company's Human Resources policies.

            Answer using only the information retrieved from the HR policy
            knowledge base.

            If the retrieved information does not contain the answer, say:
            "The available HR policies do not contain this information."

            Do not invent policies, benefits, limits, dates, or approvals.
            Keep the answer concise and clear.
            """)
    String ask(@UserMessage String question);
}
