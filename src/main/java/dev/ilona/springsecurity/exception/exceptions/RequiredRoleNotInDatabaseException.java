package dev.ilona.springsecurity.exception.exceptions;

public class RequiredRoleNotInDatabaseException extends RuntimeException {
    public RequiredRoleNotInDatabaseException(String message) {
        super(message);
    }
}
