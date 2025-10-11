package dev.ilona.springsecurity.api.user;

import dev.ilona.springsecurity.domain.user.User;
import jakarta.validation.constraints.Pattern;

public record UserRegistrationRequest(
        @Pattern(
                regexp = User.Constraints.USERNAME_PATTERN_REGEXP,
                message = User.Constraints.USERNAME_PATTERN_MESSAGE
        )
        String username,

        @Pattern(
                regexp = User.Constraints.PASSWORD_PATTERN_REGEXP,
                message = User.Constraints.PASSWORD_PATTERN_MESSAGE
        )
        String password
) {}
