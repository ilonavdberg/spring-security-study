package dev.ilona.springsecurity.utils;

import dev.ilona.springsecurity.infrastructure.email.EmailCommand;
import dev.ilona.springsecurity.infrastructure.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailSender emailSender;

    @Value("${application.email.domain}")
    private String emailDomain;

    private static final String GENERAL_INBOX = "info";

    @Async
    public void sendInviteEmail(String email, String token) {
        EmailCommand emailCommand = new EmailCommand(
                emailForInbox(GENERAL_INBOX),
                email,
                "You're invited to register",
                buildInviteBody(token)
        );
        emailSender.sendEmail(emailCommand);
    }

    private String buildInviteBody(String token) {
        return """
                Hello,

                You have been invited to register an account.
                Registration token: %s

                """.formatted(token);
    }

    private String emailForInbox(String inbox) {
        return inbox + "@" + emailDomain;
    }
}
