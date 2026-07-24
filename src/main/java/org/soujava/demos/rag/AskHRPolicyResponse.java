package org.soujava.demos.rag;

public record AskHRPolicyResponse(
        String question,
        String answer
) {
}