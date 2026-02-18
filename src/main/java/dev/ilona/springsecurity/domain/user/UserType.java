package dev.ilona.springsecurity.domain.user;

public enum UserType {
    INTERNAL,
    EXTERNAL;

    public boolean isInternal() {
        return this == INTERNAL;
    }

    public boolean isExternal() {
        return this == EXTERNAL;
    }
}
