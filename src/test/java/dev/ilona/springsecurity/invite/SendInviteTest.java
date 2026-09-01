package dev.ilona.springsecurity.invite;

import dev.ilona.springsecurity.application.user.InviteManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import dev.ilona.springsecurity.config.TestDataInitializer;
import dev.ilona.springsecurity.config.email.Mailbox;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteRepository;
import dev.ilona.springsecurity.infrastructure.email.EmailCommand;
import dev.ilona.springsecurity.infrastructure.email.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Import({
        PostgresTestContainerConfig.class,
        TestDataInitializer.class
})
public class SendInviteTest {

    @Autowired
    private InviteManagementService inviteManagementService;

    @Autowired
    private InviteRepository inviteRepository;

    @MockitoBean
    private EmailService emailService;

    @Value("${application.email.domain}")
    private String internalEmailDomain;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldSendInviteSuccessfully() {
        UUID uuid = inviteManagementService.createInviteForAdminUser(validInternalEmail());
        inviteManagementService.sendInvite(uuid);

        Invite invite = inviteRepository.findByUuid(uuid).orElseThrow();
        assertThat(invite.getStatus()).isEqualTo(Invite.Status.SENT);

        ArgumentCaptor<EmailCommand> emailCaptor = ArgumentCaptor.forClass(EmailCommand.class);
        verify(emailService).sendEmail(emailCaptor.capture());
        EmailCommand emailCommand = emailCaptor.getValue();

        assertThat(emailCommand.to()).isEqualTo(invite.getEmail());
        assertThat(emailCommand.body()).contains(invite.getToken());
        assertThat(emailCommand.from()).startsWith(Mailbox.NO_REPLY.getValue());
    }

    private String validInternalEmail() {
        return "test@" + internalEmailDomain;
    }
}
