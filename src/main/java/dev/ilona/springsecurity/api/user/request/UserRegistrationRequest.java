package dev.ilona.springsecurity.api.user.request;

import dev.ilona.springsecurity.api.user.validation.ValidUser;

@ValidUser
public record UserRegistrationRequest(
        String username,
        String email,
        String password
) {}
