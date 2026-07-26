/**
 * Data Transfer Objects (DTOs) exchanged over the REST API of the
 * {@code mongodb-rag} sample.
 *
 * <p>Each type is an immutable {@code record} serialized to and from JSON as the
 * request or response body of an endpoint under {@code /hr/policies}. Incoming
 * payloads carry Jakarta Bean Validation constraints (for example
 * {@code @NotBlank}) so invalid requests are rejected before reaching the
 * service layer.
 *
 * <h2>Payloads</h2>
 * <ul>
 *   <li>{@link org.soujava.demos.rag.dto.HRPolicyQuestion} &mdash; request body
 *       of {@code POST /hr/policies/ask}, carrying the user's question.</li>
 *   <li>{@link org.soujava.demos.rag.dto.HRPolicyAnswer} &mdash; response body
 *       of {@code POST /hr/policies/ask}, echoing the question and the generated
 *       answer.</li>
 *   <li>{@link org.soujava.demos.rag.dto.HRPolicyContextRequest} &mdash; request
 *       body of {@code POST /hr/policies/context}, carrying policy text to ingest
 *       into the knowledge base.</li>
 *   <li>{@link org.soujava.demos.rag.dto.HRPolicyContextResponse} &mdash;
 *       response body of {@code POST /hr/policies/context}, reporting whether the
 *       context was inserted along with a human-readable message.</li>
 * </ul>
 *
 * @see org.soujava.demos.rag
 */
package org.soujava.demos.rag.dto;
