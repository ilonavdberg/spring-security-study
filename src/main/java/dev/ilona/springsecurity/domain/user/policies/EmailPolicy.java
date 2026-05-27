package dev.ilona.springsecurity.domain.user.policies;

import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.exception.exceptions.PolicyViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class EmailPolicy {

    @Value("${application.email.domain}")
    private String internalEmailDomain;

    public void validate(String email, UserType userType) {
        boolean isInternalEmail = email.endsWith("@" + internalEmailDomain);

        if (userType.isInternal() && !isInternalEmail) {
            throw new PolicyViolationException("Internal users must use email addresses ending with: @" + internalEmailDomain);
        }

        if (userType.isExternal() && isInternalEmail) {
            throw new PolicyViolationException("External users cannot use email addresses ending with: @" + internalEmailDomain);
        }
    }
}
