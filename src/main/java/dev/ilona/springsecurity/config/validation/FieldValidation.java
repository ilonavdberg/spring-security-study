package dev.ilona.springsecurity.config.validation;

public record FieldValidation(
    int minLength,
    int maxLength,
    PatternValidation pattern
) {
    public record PatternValidation(
            String regex,
            String message
    ) {}
}
