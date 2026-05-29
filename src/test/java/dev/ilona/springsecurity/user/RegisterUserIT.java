package dev.ilona.springsecurity.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import dev.ilona.springsecurity.config.TestDataInitializer;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.DuplicateEntryException;
import dev.ilona.springsecurity.exception.exceptions.PolicyViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Import({
        PostgresTestContainerConfig.class,
        TestDataInitializer.class
})
public class RegisterUserIT {

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    UserRepository userRepository;

    @Value("${application.email.domain}")
    private String internalEmailDomain;

    private final String duplicateUsername = "duplicate-user";
    private final String duplicateEmail = "duplicate@email.com";

    @BeforeEach
    void setup() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                duplicateUsername,
                duplicateEmail,
                "P@ssW0rd"
        );

        userManagementService.registerExternalUser(request);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "test-user",
                "test-user@email.com",
                "P@ssW0rd"
        );

        UUID uuid = userManagementService.registerExternalUser(request);

        User savedUser = userRepository.findByUuid(uuid)
                .orElseThrow();

        assertThat(savedUser).satisfies(user -> {
            assertThat(user.getUsername()).isEqualTo(request.username());
            assertThat(user.getEmail()).isEqualTo(request.email());
            assertThat(user.getPassword()).isNotBlank();
            assertThat(user.getPassword()).isNotEqualTo(request.password()); // stored password must be encoded

            assertThat(user.getRoles()).extracting(Role::getName)
                    .contains(Role.RoleName.ROLE_USER)
                    .doesNotContain(Role.RoleName.ROLE_ADMIN);

            assertThat(user.isBlocked()).isFalse();
            assertThat(user.isDeleted()).isFalse();
        });
    }

    @Test
    void shouldNotRegisterUserWhenUsernameAlreadyExists() {
        String email = "test@email.com";
        UserRegistrationRequest request = new UserRegistrationRequest(
                duplicateUsername,
                email,
                "P@ssW0rd"
        );

        assertThatThrownBy(() -> userManagementService.registerExternalUser(request))
                .isInstanceOf(DuplicateEntryException.class)
                .hasMessageMatching("(?i).*username.*");

        assertThat(userRepository.existsByEmail(email)).isFalse();
    }

    @Test
    void shouldNotRegisterUserWhenEmailAlreadyExists() {
        String username = "username";
        UserRegistrationRequest request = new UserRegistrationRequest(
                username,
                duplicateEmail,
                "P@ssW0rd"
        );

        assertThatThrownBy(() -> userManagementService.registerExternalUser(request))
                .isInstanceOf(DuplicateEntryException.class)
                .hasMessageMatching("(?i).*email.*");

        assertThat(userRepository.existsByUsername(username)).isFalse();
    }

    @Test
    void shouldNotRegisterUserWithInternalEmailDomain() {
        String username = "username";
        UserRegistrationRequest request = new UserRegistrationRequest(
                "username",
                "email@" + internalEmailDomain,
                "P@ssW0rd"
        );

        assertThatThrownBy(() -> userManagementService.registerExternalUser(request))
                .isInstanceOf(PolicyViolationException.class);

        assertThat(userRepository.existsByUsername(username)).isFalse();
    }

}
