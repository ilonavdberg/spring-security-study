package dev.ilona.springsecurity.config.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Mailbox {
    GENERAL("info"),
    NO_REPLY("noreply"),
    ADMIN("admin");

    private final String value;

}
