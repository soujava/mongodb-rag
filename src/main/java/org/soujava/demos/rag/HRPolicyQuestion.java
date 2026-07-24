package org.soujava.demos.rag;

import jakarta.validation.constraints.NotBlank;

public record HRPolicyQuestion(
        @NotBlank
        String question
) {
}