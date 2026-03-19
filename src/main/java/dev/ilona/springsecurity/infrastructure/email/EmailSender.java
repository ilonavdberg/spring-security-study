package dev.ilona.springsecurity.infrastructure.email;

public interface EmailSender {
    void sendEmail(EmailCommand emailCommand);
}
