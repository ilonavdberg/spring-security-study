package dev.ilona.springsecurity.exception.exceptions;

public class PolicyViolationException extends RuntimeException {
    public PolicyViolationException(String message) {
        super(message);
    }
}
