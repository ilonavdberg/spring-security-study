package dev.ilona.springsecurity.infrastructure.email;

public record EmailCommand(
        String from,
        String to,
        String subject,
        String body
) {
}
