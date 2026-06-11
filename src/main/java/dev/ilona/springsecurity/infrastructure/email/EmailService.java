package dev.ilona.springsecurity.infrastructure.email;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailSender emailSender;

    @Async
    public void sendEmail(EmailCommand emailCommand) {
        emailSender.sendEmail(emailCommand);
    }
}
