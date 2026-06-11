package dev.ilona.springsecurity.application.user;

import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteRepository;
import dev.ilona.springsecurity.domain.user.invite.InviteService;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import dev.ilona.springsecurity.infrastructure.email.EmailService;
import dev.ilona.springsecurity.infrastructure.email.EmailTemplate;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class InviteManagementService {

    private final InviteService inviteService;
    private final InviteRepository inviteRepository;
    private final RoleService roleService;
    private final EmailService emailService;

    @Value("${application.email.domain}")
    private String emailDomain;

    private final String GENERAL_INBOX = "info";

    @PreAuthorize("hasRole('ADMIN')")
    public UUID createInviteForAdminUser(String email) {
        Invite invite = inviteService.createInvite(email, roleService.getAdminRole());
        return invite.getUuid();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void sendInvite(UUID uuid) {
        Invite invite = inviteRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("No invite found with uuid: " + uuid));

        invite.assertCanBeSent();
        emailService.sendEmail(EmailTemplate.invite(
                "info@example.com",
                invite.getEmail(),
                invite.getToken()
        ));
        invite.markAsSent();
    }
}
