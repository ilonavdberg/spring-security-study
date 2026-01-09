package dev.ilona.springsecurity.config.validation.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.config.validation.FieldValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<ValidUser, UserRegistrationRequest> {

    private final UserValidationProperties userValidationProperties;

    @Override
    public boolean isValid(UserRegistrationRequest request, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean valid = true;

        if (!isUsernameValid(request.username(), context)) {
            valid = false;
        }

        if (!isPasswordValid(request.password(), context)) {
            valid = false;
        }

        if (!isEmailValid(request.email(), context)) {
            valid = false;
        }

        return valid;
    }

    private boolean isUsernameValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        FieldValidation usernameRules = userValidationProperties.getUsername();
        boolean valid = true;

        if (username.length() < usernameRules.minLength()) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Username must be at least " + usernameRules.minLength() + " characters")
                    .addPropertyNode("username")
                    .addConstraintViolation();
            valid = false;
        }

        if (username.length() > usernameRules.maxLength()) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Username must be at most " + usernameRules.maxLength() + " characters")
                    .addPropertyNode("username")
                    .addConstraintViolation();
            valid = false;
        }

        if (!username.matches(usernameRules.pattern().regex())) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(usernameRules.pattern().message())
                    .addPropertyNode("username")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }

    private boolean isPasswordValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        FieldValidation passwordRules = userValidationProperties.getPassword();
        boolean valid = true;

        if (password.length() < passwordRules.minLength()) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Password must be at least " + passwordRules.minLength() + " characters")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            valid = false;
        }

        if (password.length() > passwordRules.maxLength()) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Password must be at most " + passwordRules.maxLength() + " characters")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            valid = false;
        }

        if (!password.matches(passwordRules.pattern().regex())) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(passwordRules.pattern().message())
                    .addPropertyNode("password")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }

    private boolean isEmailValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        FieldValidation emailRules = userValidationProperties.getEmail();
        boolean valid = true;

        if (!email.matches(emailRules.pattern().regex())) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(emailRules.pattern().message())
                    .addPropertyNode("email")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
