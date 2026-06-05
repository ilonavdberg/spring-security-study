package dev.ilona.springsecurity.application.auth;

public record TokenPair(String refreshToken, String accessToken) {
    public static TokenPair of(String refreshToken, String accessToken) {
        return new TokenPair(refreshToken, accessToken);
    }
}
