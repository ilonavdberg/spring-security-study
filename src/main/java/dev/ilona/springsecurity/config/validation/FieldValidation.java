package dev.ilona.springsecurity.config.validation;

public record FieldValidation(
    int minLength,
    int maxLength,
    String regex
) { }
