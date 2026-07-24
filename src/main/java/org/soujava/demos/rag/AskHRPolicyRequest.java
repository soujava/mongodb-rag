package org.soujava.demos.rag;

import jakarta.validation.constraints.NotBlank;

public record AskHRPolicyRequest(
        @NotBlank
        String question
) {
}