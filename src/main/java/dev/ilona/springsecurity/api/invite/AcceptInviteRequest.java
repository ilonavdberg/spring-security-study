package dev.ilona.springsecurity.api.invite;

import dev.ilona.springsecurity.domain.user.password.PasswordPolicy;
import jakarta.validation.constraints.*;

public record AcceptInviteRequest(
        @NotBlank(message = "Email must be provided.")
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
) {
}
