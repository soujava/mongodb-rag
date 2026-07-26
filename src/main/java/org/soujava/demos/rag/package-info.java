/**
 * Root package of the {@code mongodb-rag} sample, a Helidon MP application that
 * demonstrates the Retrieval-Augmented Generation (RAG) pattern for answering
 * natural-language questions about HR policies.
 *
 * <p>Policy text is embedded into vectors and stored in MongoDB Atlas. At query
 * time the most relevant passages are retrieved and passed to the chat model as
 * context, so answers stay grounded in the ingested knowledge base rather than
 * the model's prior knowledge.
 *
 * <h2>Main components</h2>
 * <ul>
 *   <li>{@link org.soujava.demos.rag.HRPolicyResource} &mdash; JAX-RS endpoints
 *       to ask questions ({@code POST /hr/policies/ask}) and ingest policy
 *       context ({@code POST /hr/policies/context}).</li>
 *   <li>{@link org.soujava.demos.rag.HRPolicyService} &mdash; orchestrates a
 *       question through the RAG agent and builds the answer.</li>
 *   <li>{@link org.soujava.demos.rag.HRPolicyContextService} &mdash; ingests new
 *       policy text into the vector store, skipping near-duplicates.</li>
 *   <li>{@link org.soujava.demos.rag.HRPolicyAgent} &mdash; the LangChain4j AI
 *       service that generates grounded answers.</li>
 *   <li>Request/response records &mdash;
 *       {@link HRPolicyQuestion},
 *       {@link HRPolicyAnswer}, and their context
 *       counterparts, used as JSON payloads.</li>
 * </ul>
 *
 * <p>The CDI producers that wire the embedding model, vector store, retriever,
 * and agent live in {@link org.soujava.demos.rag.producer}.
 *
 * @see org.soujava.demos.rag.producer
 */
package org.soujava.demos.rag;

import org.soujava.demos.rag.dto.HRPolicyAnswer;
import org.soujava.demos.rag.dto.HRPolicyQuestion;