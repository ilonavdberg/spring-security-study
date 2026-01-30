package dev.ilona.springsecurity.domain.user.refreshtoken;

import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenGenerator tokenGenerator;

    @Value("${security.jwt.refresh-token.valid-period}")
    private final Duration validPeriod;

    @Value("${security.jwt.refresh-token.byte-length}")
    private final int tokenByteLength;

    public RefreshToken createFor(User user) {
        String token = tokenGenerator.generate(tokenByteLength);
        Instant expirationDate = Instant.now().plus(validPeriod);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expirationDate(expirationDate)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}
