/**
 * CDI producers that assemble and wire the Retrieval-Augmented Generation (RAG)
 * pipeline used by the {@code mongodb-rag} sample.
 *
 * <p>Each producer is an {@code @ApplicationScoped} bean that exposes a single
 * collaborator through an {@code @Produces} method, so the rest of the
 * application can inject the pipeline pieces without knowing how they are built.
 * Configuration values (model names, MongoDB coordinates, retrieval thresholds)
 * are read from MicroProfile Config via {@code @ConfigProperty}, keeping wiring
 * separate from tuning.
 *
 * <h2>Producers</h2>
 * <ul>
 *   <li>{@code EmbeddingModelProducer} &mdash; builds the OpenAI
 *       {@link dev.langchain4j.model.embedding.EmbeddingModel} that turns text
 *       into vectors.</li>
 *   <li>{@code VectorStoreProducer} &mdash; builds the MongoDB Atlas
 *       {@link dev.langchain4j.store.embedding.EmbeddingStore} that persists and
 *       searches those vectors.</li>
 *   <li>{@code ContentRetrieverProducer} &mdash; builds the
 *       {@link dev.langchain4j.rag.content.retriever.ContentRetriever} that
 *       queries the vector store for passages relevant to a question.</li>
 *   <li>{@code HRPolicyAgentProducer} &mdash; binds the chat model and retriever
 *       into the {@link org.soujava.demos.rag.HRPolicyAgent} that generates
 *       grounded answers.</li>
 * </ul>
 *
 * <p>The classes in this package are package-private on purpose: they are CDI
 * beans discovered by the container, not part of the application's public API.
 *
 * @see org.soujava.demos.rag
 */
package org.soujava.demos.rag.producer;
