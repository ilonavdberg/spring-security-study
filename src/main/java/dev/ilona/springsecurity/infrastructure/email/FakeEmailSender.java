package dev.ilona.springsecurity.infrastructure.email;

import org.springframework.stereotype.Component;

@Component
public class FakeEmailSender implements EmailSender {

    @Override
    public void sendEmail(EmailCommand command) {
        String email = """
                From: %s
                To: %s
                Subject: %s
                
                %s
                """.formatted(command.from(), command.to(), command.subject(), command.body());


        System.out.println(email);
    }
}
