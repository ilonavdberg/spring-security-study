package dev.ilona.spring_security_study.api.auth.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
