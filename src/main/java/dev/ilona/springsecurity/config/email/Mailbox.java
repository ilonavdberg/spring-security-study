package dev.ilona.springsecurity.config.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Mailbox {
    GENERAL("info");

    private final String value;

}
