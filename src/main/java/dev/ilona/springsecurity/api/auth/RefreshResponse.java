package dev.ilona.springsecurity.api.auth;

public record RefreshResponse(String refreshToken, String accessToken) {
    public static RefreshResponse of(String refreshToken, String accessToken) {
        return new RefreshResponse(refreshToken, accessToken);
    }
}
