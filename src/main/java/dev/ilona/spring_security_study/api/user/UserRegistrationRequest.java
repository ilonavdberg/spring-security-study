package dev.ilona.spring_security_study.api.user;

import dev.ilona.spring_security_study.domain.user.User;
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
