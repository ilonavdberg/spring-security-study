package dev.ilona.springsecurity.api.user;

import dev.ilona.springsecurity.config.validation.user.ValidUser;

@ValidUser
public record UserRegistrationRequest(
        String username,
        String email,
        String password
) {}
