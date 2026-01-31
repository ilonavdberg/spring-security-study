package dev.ilona.springsecurity.api.invite;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendInviteRequest(
        @NotBlank(message = "Email must be provided.")
        @Email(message = "Email must be a valid email address.")
        String email
) {}
