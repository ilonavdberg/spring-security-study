package dev.ilona.springsecurity.user;

import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import dev.ilona.springsecurity.config.TestDataInitializer;
import dev.ilona.springsecurity.domain.user.AuthenticationMethod;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import dev.ilona.springsecurity.exception.exceptions.IllegalStateTransitionException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    RoleService roleService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldBlockExternalUserSuccessfully() {
        User externalUser = userRepository.save(
                User.builder()
                        .username("external-user")
                        .email("external@email.com")
                        .userType(UserType.EXTERNAL)
                        .roles(List.of(roleService.getStandardUserRole()))
                        .authenticationMethod(AuthenticationMethod.PASSWORD)
                        .build()
        );

        assertThat(externalUser.isBlocked())
                .as("Precondition: user must not be blocked at start.")
                .isFalse();

        userManagementService.blockUser(externalUser.getUuid());

        User updatedUser = userRepository.findByUuid(externalUser.getUuid())
                .orElseThrow();

        assertThat(updatedUser.isBlocked()).isTrue();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotBlockUserWhenUserIsInternal() {
        User internalUser = userRepository.save(
                User.builder()
                        .username("internal-user")
                        .email("internal@email.com")
                        .userType(UserType.INTERNAL)
                        .roles(List.of(roleService.getAdminRole()))
                        .authenticationMethod(AuthenticationMethod.PASSWORD)
                        .build()
        );

        assertThatThrownBy(() -> userManagementService.blockUser(internalUser.getUuid()))
                .isInstanceOf(IllegalStateTransitionException.class);

        User updatedUser = userRepository.findByUuid(internalUser.getUuid())
                        .orElseThrow();

        assertThat(updatedUser.isBlocked()).isFalse();
    }


    @Test
    @WithMockUser(roles = "USER")
    void shouldNotAllowNonAdminUserToBlockOtherUsers() {
        User externalUser = userRepository.save(
                User.builder()
                        .username("external-user")
                        .email("external@email.com")
                        .userType(UserType.EXTERNAL)
                        .roles(List.of(roleService.getStandardUserRole()))
                        .authenticationMethod(AuthenticationMethod.PASSWORD)
                        .build()
        );

        assertThatThrownBy(() -> userManagementService.blockUser(externalUser.getUuid()))
                .isInstanceOf(AuthorizationDeniedException.class);

        User updatedUser = userRepository.findByUuid(externalUser.getUuid())
                .orElseThrow();

        assertThat(updatedUser.isBlocked()).isFalse();

    }
}
