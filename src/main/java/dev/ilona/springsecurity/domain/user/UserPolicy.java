package dev.ilona.springsecurity.domain.user;

import dev.ilona.springsecurity.config.validation.FieldValidation;
import dev.ilona.springsecurity.config.validation.user.UserValidationProperties;
import dev.ilona.springsecurity.exception.exceptions.PolicyViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPolicy {

    private final UserValidationProperties userValidationProperties;

    public void assertValidUsername(String username) {
        FieldValidation usernameRules = userValidationProperties.getUsername();

        if (username.length() < usernameRules.minLength()) {
            throw new PolicyViolationException("Username must be at least " + usernameRules.minLength() + " characters");
        }

        if (username.length() > usernameRules.maxLength()) {
            throw new PolicyViolationException("Username must be at most " + usernameRules.maxLength() + " characters");
        }

        if (!username.matches(usernameRules.pattern().regex())) {
            throw new PolicyViolationException(usernameRules.pattern().message());
        }
    }

    public void assertValidPassword(String password) {
        FieldValidation passwordRules = userValidationProperties.getPassword();

        if (password.length() < passwordRules.minLength()) {
            throw new PolicyViolationException("Password must be at least " + passwordRules.minLength() + " characters");
        }

        if (password.length() > passwordRules.maxLength()) {
            throw new PolicyViolationException("Password must be at most " + passwordRules.maxLength() + " characters");
        }

        if (!password.matches(passwordRules.pattern().regex())) {
            throw new PolicyViolationException(passwordRules.pattern().message());
        }
    }

    public void assertValidEmail(String email) {
        FieldValidation emailRules = userValidationProperties.getEmail();

        if (!email.matches(emailRules.pattern().regex())) {
            throw new PolicyViolationException(emailRules.pattern().message());
        }
    }
}
