package dev.ilona.springsecurity.api.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank String token) {
}
