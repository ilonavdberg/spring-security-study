package dev.ilona.springsecurity.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TokenGenerator {

    private final SecureRandom secureRandom;
    private final Base64.Encoder base64Encoder;

    /**
     * Generates a URL-safe secure random token.
     *
     * @param tokenByteLength the number of random bytes used to generate the token
     * @return the generated token
     */
    public String generate(int tokenByteLength) {
        byte[] bytes = new byte[tokenByteLength];
        secureRandom.nextBytes(bytes);
        return base64Encoder.encodeToString(bytes);
    }
}
