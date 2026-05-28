package dev.ilona.springsecurity.api.user;

import dev.ilona.springsecurity.domain.user.policies.PasswordPolicy;
import jakarta.validation.constraints.*;

public record UserRegistrationRequest(
        @NotBlank(message = "Username is required.")
        @Size(
                min = 8,
                max = 32,
                message = "Username must be between 8 and 32 characters."
        )
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Username can only contain letters, digits, underscores, and hyphens, and no spaces.")
        String username,

        @NotBlank(message = "Email is required.")
        @Email(message = "Email must be a valid email address.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(
                min = PasswordPolicy.MIN_LENGTH,
                max = PasswordPolicy.MAX_LENGTH,
                message = "Password must be between " + PasswordPolicy.MIN_LENGTH + " and " + PasswordPolicy.MAX_LENGTH + " characters."
        )
        @Pattern(regexp = PasswordPolicy.STRONG_PASSWORD_PATTERN, message = PasswordPolicy.STRONG_PASSWORD_MESSAGE)
        String password
) {}
