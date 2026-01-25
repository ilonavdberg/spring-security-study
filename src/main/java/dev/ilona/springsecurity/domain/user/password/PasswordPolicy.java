package dev.ilona.springsecurity.domain.user.password;

import dev.ilona.springsecurity.exception.exceptions.PolicyViolationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PasswordPolicy {
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 50;
    public static final String STRONG_PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$";
    public static final String STRONG_PASSWORD_MESSAGE = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace.";

    public static void assertValidPassword(String password) {
        if (password == null) {
            throw new PolicyViolationException("Password is required");
        }

        if (password.length() < MIN_LENGTH) {
            throw new PolicyViolationException("Password must be at least " + MIN_LENGTH + " characters");
        }

        if (password.length() > MAX_LENGTH) {
            throw new PolicyViolationException("Password must be at most " + MAX_LENGTH + " characters");
        }

        if (!password.matches(STRONG_PASSWORD_PATTERN)) {
            throw new PolicyViolationException(STRONG_PASSWORD_MESSAGE);
        }
    }
}
