package org.soujava.demos.rag.dto;

import jakarta.validation.constraints.NotBlank;

public record HRPolicyContextRequest(@NotBlank String context) {
}
