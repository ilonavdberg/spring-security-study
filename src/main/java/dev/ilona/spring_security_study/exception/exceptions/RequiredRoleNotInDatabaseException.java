package dev.ilona.spring_security_study.exception.exceptions;

public class RequiredRoleNotInDatabaseException extends RuntimeException {
    public RequiredRoleNotInDatabaseException(String message) {
        super(message);
    }
}
