package dev.ilona.springsecurity.api.auth;

public record LoginResponse(String token) {
    public static LoginResponse of(String token) {
        return new LoginResponse(token);
    }
}
