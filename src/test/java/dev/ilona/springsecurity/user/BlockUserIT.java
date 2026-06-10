package dev.ilona.springsecurity.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import dev.ilona.springsecurity.config.TestDataInitializer;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.exception.exceptions.IllegalStateTransitionException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
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
public class BlockUserIT {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "test-user",
                "test-user@email.com",
                "P@ssW0rd"
        );

        userManagementService.registerUser(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldBlockUserSuccessfully() {
        User user = userRepository.findByUsernameAndDeletedFalse("test-user")
                .orElseThrow();

        assertThat(user).satisfies(u -> {
            assertThat(u.isBlocked())
                    .as("Precondition: user must not be blocked at start.")
                    .isFalse();
            assertThat(u.getUserType())
                    .as("Precondition: user must be external.")
                    .isEqualTo(UserType.EXTERNAL);
        });

        UUID userUuid = user.getUuid();
        userManagementService.blockUser(userUuid);

        User updatedUser = userRepository.findByUuid(userUuid)
                .orElseThrow();

        assertThat(updatedUser.isBlocked()).isTrue();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotBlockUserWhenUserIsInternal() {
        entityManager.createNativeQuery("""
                    UPDATE users
                    SET user_type = :type
                    WHERE username = 'test-user'
                """)
                .setParameter("type", UserType.INTERNAL.name())
                .executeUpdate();

        entityManager.flush();
        entityManager.clear();

        User user = userRepository.findByUsernameAndDeletedFalse("test-user")
                .orElseThrow();
        UUID userUuid = user.getUuid();

        assertThatThrownBy(() -> userManagementService.blockUser(userUuid))
                .isInstanceOf(IllegalStateTransitionException.class);

        assertThat(user.isBlocked()).isFalse();
    }


    void shouldNotAllowNonAdminUserToBlockOtherUsers() {}
}
