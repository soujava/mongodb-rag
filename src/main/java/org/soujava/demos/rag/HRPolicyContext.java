package org.soujava.demos.rag;

import jakarta.validation.constraints.NotBlank;

public record HRPolicyContext(@NotBlank String context) {
}
