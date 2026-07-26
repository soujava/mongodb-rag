package org.soujava.demos.rag.dto;

import jakarta.validation.constraints.NotBlank;

public record HRPolicyQuestion(
        @NotBlank
        String question
) {
}