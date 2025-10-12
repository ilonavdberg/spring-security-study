package dev.ilona.spring_security_study.api.auth.request;

public record LoginRequest(
        String username,
        String password
) {}
