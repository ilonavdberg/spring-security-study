package dev.ilona.springsecurity.config.validation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "validation.user")
@NoArgsConstructor
@Setter
@Getter
public class UserValidationProperties {
    private FieldValidation username;
    private FieldValidation password;
    private FieldValidation email;
}
