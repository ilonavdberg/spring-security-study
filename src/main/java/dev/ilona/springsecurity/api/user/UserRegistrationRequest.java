package dev.ilona.springsecurity.api.user;

import dev.ilona.springsecurity.domain.user.password.PasswordPolicy;
import jakarta.validation.constraints.*;

public record UserRegistrationRequest(
        @NotNull(message = "Username is required.")
        @Size(
                min = 8,
                max = 32,
                message = "Username must be between 8 and 32 characters."
        )
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Username can only contain letters, digits, underscores, and hyphens, and no spaces.")
        String username,

        @NotNull(message = "Email is required.")
        @Email(message = "Email must be a valid email address.")
        String email,

        @NotNull(message = "Password is required.")
        @Size(
                min = PasswordPolicy.MIN_LENGTH,
                max = PasswordPolicy.MAX_LENGTH,
                message = "Password must be between " + PasswordPolicy.MIN_LENGTH + " and " + PasswordPolicy.MAX_LENGTH + " characters."
        )
        @Pattern(regexp = PasswordPolicy.STRONG_PASSWORD_PATTERN, message = PasswordPolicy.STRONG_PASSWORD_MESSAGE)
        String password
) {}
