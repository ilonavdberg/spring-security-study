package dev.ilona.springsecurity.domain.user.policies;

import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.exception.exceptions.DuplicateEntryException;
import dev.ilona.springsecurity.exception.exceptions.PolicyViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class UserAccountPolicy {

    private final UserRepository userRepository;

    @Value("${application.email.domain}")
    private String internalEmailDomain;

    public void validateEmailForNewUser(String email, UserType userType) {
        validateEmailForUserType(email, userType);
        ensureEmailIsAvailable(email);
    }

    public void ensureUsernameIsAvailable(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateEntryException("Username already exists: " + username);
        }
    }

    private void ensureEmailIsAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEntryException("Email address is already in use: " + email);
        }
    }

    private void validateEmailForUserType(String email, UserType userType) {
        boolean isInternalEmail = email.endsWith("@" + internalEmailDomain);

        if (userType.isInternal() && !isInternalEmail) {
            throw new PolicyViolationException("Internal users must use email addresses ending with: @" + internalEmailDomain);
        }

        if (userType.isExternal() && isInternalEmail) {
            throw new PolicyViolationException("External users cannot use email addresses ending with: @" + internalEmailDomain);
        }
    }
}
