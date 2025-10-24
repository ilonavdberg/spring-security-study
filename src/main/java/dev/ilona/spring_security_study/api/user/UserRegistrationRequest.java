package dev.ilona.spring_security_study.api.user;

import jakarta.validation.constraints.Pattern;

public record UserRegistrationRequest(
        @Pattern(
                regexp = "\\S{8,}",
                message = "Username must be at least 8 characters long and must not contain whitespace.")
        String username,

        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "Password must be at least 8 characters and contain at least one lowercase letter, one uppercase letter, one number and one special character."
        )
        String password
) {}
