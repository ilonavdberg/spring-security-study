package dev.ilona.springsecurity.api.user;

import dev.ilona.springsecurity.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegistrationRequest(
        @Pattern(
                regexp = User.Constraints.USERNAME_PATTERN_REGEXP,
                message = User.Constraints.USERNAME_PATTERN_MESSAGE
        )
        String username,

        @Email(message = User.Constraints.EMAIL_EMAIL_MESSAGE)
        @NotBlank(message = User.Constraints.EMAIL_NOT_BLANK_MESSAGE)
        String email,

        @Pattern(
                regexp = User.Constraints.PASSWORD_PATTERN_REGEXP,
                message = User.Constraints.PASSWORD_PATTERN_MESSAGE
        )
        String password
) {}
