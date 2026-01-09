package dev.ilona.springsecurity.config.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "validation.user")
@AllArgsConstructor
@Getter
public class UserValidationProperties {
    private final FieldValidation username;
    private final FieldValidation password;
}
