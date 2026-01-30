package dev.ilona.springsecurity.domain.user.refreshtoken;

import dev.ilona.springsecurity.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom;
    private final Base64.Encoder base64Encoder;

    @Value("${security.jwt.refresh-token.valid-period}")
    private final Duration expiresAfter;

    @Value("${security.jwt.refresh-token.byte-length}")
    private final int tokenByteLength;

    public RefreshToken createFor(User user) {
        String token = generateToken();
        Instant expirationDate = Instant.now().plus(expiresAfter);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expirationDate(expirationDate)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private String generateToken() {
        byte[] bytes = new byte[tokenByteLength];
        secureRandom.nextBytes(bytes);
        return base64Encoder.encodeToString(bytes);
    }
}
