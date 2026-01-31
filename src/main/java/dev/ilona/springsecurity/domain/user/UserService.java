package dev.ilona.springsecurity.domain.user;

import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.DuplicateEntryException;
import dev.ilona.springsecurity.exception.exceptions.PolicyViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.ilona.springsecurity.domain.user.password.PasswordPolicy.assertValidPassword;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.email.domain}")
    private String internalEmailDomain;

    /**
     * Creates a new user with password-based authentication.
     *
     * @param username    the username chosen by the user
     * @param rawPassword the plain text password provided by the user
     * @param email       the user's email address
     * @param roles        the roles assigned to the new user
     * @return the created {@link User} instance
     */
    public User createUser(String username, String rawPassword, String email, List<Role> roles) {
        return createUser(AuthenticationMethod.PASSWORD, username, rawPassword, email, roles);
    }

    /**
     * Creates a new OAuth2 user.
     * <ul>
     *   <li>Username is set to the email to satisfy Spring Security's {@code UserDetailsService}, which requires a non-null username.</li>
     *   <li>Password is {@code null} because OAuth2 users do not have a password.</li>
     *   <li>Email is the actual email of the user.</li>
     * </ul>
     *
     * @param email the user's email, also used as username
     * @param roles  the roles assigned to the new user
     * @return the created {@link User} instance
     */
    public User createUser(String email, List<Role> roles) {
        return createUser(AuthenticationMethod.OAUTH2, email, null, email, roles);
    }

    private User createUser(AuthenticationMethod authenticationMethod, String username, String rawPassword, String email, List<Role> roles) {
        for (Role role : roles) {
            validateEmailForRole(email, role);
        }

        String password = switch (authenticationMethod) {
            case PASSWORD -> {
                assertValidPassword(rawPassword);
                yield passwordEncoder.encode(rawPassword);
            }
            case OAUTH2 -> {
                requireNoPasswordForOauth2(rawPassword);
                yield null;
            }
        };

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateEntryException("Username already exists: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEntryException("Email address is already in use: " + email);
        }

        User user = User.builder()
                .authenticationMethod(authenticationMethod)
                .username(username)
                .password(password)
                .email(email)
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    public void validateEmailForRole(String email, Role role) {
        boolean isInternalEmail = email.endsWith(internalEmailDomain);

        if (role.isInternal() && !isInternalEmail) {
            throw new PolicyViolationException("Internal users must use email addresses ending with: @" + internalEmailDomain);
        }

        if (!role.isInternal() && isInternalEmail) {
            throw new PolicyViolationException("External users cannot use email addresses ending with: @" + internalEmailDomain);
        }
    }

    private static void requireNoPasswordForOauth2(String password) {
        if (password != null) {
            throw new PolicyViolationException("Password must be null for OAUTH2 authentication.");
        }
    }
}
