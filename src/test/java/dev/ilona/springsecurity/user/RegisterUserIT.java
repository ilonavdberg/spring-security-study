package dev.ilona.springsecurity.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.common.PostgresTestContainerConfig;
import dev.ilona.springsecurity.common.TestDataInitializer;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.role.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import({
        PostgresTestContainerConfig.class,
        TestDataInitializer.class
})
public class RegisterUserIT {

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "test-user",
                "test-user@email.com",
                "P@ssw0rd"
        );

        UUID uuid = userManagementService.registerUser(request);

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

}
