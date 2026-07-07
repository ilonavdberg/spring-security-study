package dev.ilona.springsecurity.invite;

import dev.ilona.springsecurity.application.user.InviteManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import dev.ilona.springsecurity.config.TestDataInitializer;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteRepository;
import dev.ilona.springsecurity.domain.user.role.Role;
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
    InviteRepository inviteRepository;

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
    public void shouldNotCreateInviteForExternalEmailAddress() {
        assertThatThrownBy(() -> inviteManagementService.createInviteForAdminUser("test@external-domain.com"))
                .isInstanceOf(PolicyViolationException.class)
                .hasMessageMatching("(?i).*email.*")
                .hasMessageMatching("(?i).*internal.*");
    }

    private String validInternalEmail() {
        return "test@" + internalEmailDomain;
    }
}

