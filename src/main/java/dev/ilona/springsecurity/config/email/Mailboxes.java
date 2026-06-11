package dev.ilona.springsecurity.config.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Mailboxes {

    @Value("${application.email.domain}")
    private String domain;

    public String addressOf(Mailbox mailbox) {
        return mailbox.getValue() + "@" + domain;
    }
}
