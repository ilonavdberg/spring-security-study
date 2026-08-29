package dev.ilona.springsecurity.invite;

import dev.ilona.springsecurity.application.user.InviteManagementService;
import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import dev.ilona.springsecurity.config.TestDataInitializer;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteRepository;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.DuplicateEntryException;
import dev.ilona.springsecurity.exception.exceptions.PolicyViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Import({
        PostgresTestContainerConfig.class,
        TestDataInitializer.class
})
public class CreateInviteIT {

    @Autowired
    private InviteManagementService inviteManagementService;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    InviteRepository inviteRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${application.email.domain}")
    private String internalEmailDomain;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldCreateAdminInviteSuccessfully() {
        String email = validInternalEmail();
        Instant now = Instant.now();
        UUID uuid = inviteManagementService.createInviteForAdminUser(email);

        Invite savedInvite = inviteRepository.findByUuid(uuid)
                .orElseThrow();

        assertThat(savedInvite).satisfies(invite -> {
            assertThat(invite.getEmail()).isEqualTo(email);
            assertThat(invite.getStatus()).isEqualTo(Invite.Status.NEW);
            assertThat(invite.getToken()).isNotBlank();

            assertThat(invite.getRoles()).extracting(Role::getName)
                    .contains(Role.RoleName.ROLE_ADMIN);

            assertThat(invite.getExpirationDate())
                    .isCloseTo(now.plus(Duration.ofDays(1)), within(2, ChronoUnit.SECONDS));
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldRejectInviteCreationByNonAdminUser() {
        assertThatThrownBy(() -> inviteManagementService.createInviteForAdminUser(validInternalEmail()))
                .isInstanceOf(AuthorizationDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldRejectInviteCreationForExternalEmailAddress() {
        assertThatThrownBy(() -> inviteManagementService.createInviteForAdminUser("test@external-domain.com"))
                .isInstanceOf(PolicyViolationException.class)
                .hasMessageMatching("(?i).*email.*")
                .hasMessageMatching("(?i).*internal.*");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldRejectInviteCreationForExistingUserEmail() {
        String email = validInternalEmail();

        UUID inviteUuid = inviteManagementService.createInviteForAdminUser(email);
        Invite invite = inviteRepository.findByUuid(inviteUuid)
                .orElseThrow();
        invite.markAsSent();
        UUID userUuid = userManagementService.createUserFromInvite(email, "P@ssW0rd", invite.getToken());

        assertThat(userRepository.findByUuid(userUuid)).isPresent(); //verify test setup was successful

        assertThatThrownBy(() -> inviteManagementService.createInviteForAdminUser(email))
                .isInstanceOf(DuplicateEntryException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldRejectInviteCreationWhenActiveInviteAlreadyExists() {
        String email = validInternalEmail();

        UUID uuid = inviteManagementService.createInviteForAdminUser(email);
        assertThat(inviteRepository.findByUuid(uuid).isPresent());

        assertThatThrownBy(() -> inviteManagementService.createInviteForAdminUser(email))
                .isInstanceOf(DuplicateEntryException.class)
                .hasMessageContaining("invite")
                .hasMessageContaining("email");
    }

    private String validInternalEmail() {
        return "test@" + internalEmailDomain;
    }
}

