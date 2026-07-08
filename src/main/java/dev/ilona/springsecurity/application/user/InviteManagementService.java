package dev.ilona.springsecurity.application.user;

import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteRepository;
import dev.ilona.springsecurity.domain.user.invite.InviteService;
import dev.ilona.springsecurity.domain.user.policies.UserAccountPolicy;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import dev.ilona.springsecurity.config.email.Mailboxes;
import dev.ilona.springsecurity.config.email.Mailbox;
import dev.ilona.springsecurity.infrastructure.email.EmailService;
import dev.ilona.springsecurity.config.email.EmailTemplate;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final UserAccountPolicy userAccountPolicy;
    private final UserRepository userRepository;
    private final Mailboxes mailboxes;

    @PreAuthorize("hasRole('ADMIN')")
    public UUID createInviteForAdminUser(String email) {
        userAccountPolicy.validateEmailForNewUser(email, UserType.INTERNAL);
        Invite invite = inviteService.create(email, roleService.getAdminRole());
        return invite.getUuid();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void sendInvite(UUID uuid) {
        Invite invite = inviteRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("No invite found with uuid: " + uuid));

        invite.assertCanBeSent();
        emailService.sendEmail(EmailTemplate.invite(
                mailboxes.addressOf(Mailbox.GENERAL),
                invite.getEmail(),
                invite.getToken()
        ));
        invite.markAsSent();
    }
}
