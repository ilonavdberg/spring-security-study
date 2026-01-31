package dev.ilona.springsecurity.domain.user.invite;

import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.InvalidInviteOperationException;
import dev.ilona.springsecurity.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final UserService userService;
    private final TokenGenerator tokenGenerator;

    @Value("${security.invite.token.valid-period}")
    private final Duration validPeriod;

    @Value("${security.invite.token.byte-length}")
    private final int tokenByteLength;

    public Invite createInvite(String email, Role role) {
        userService.validateEmailForRole(email, role);

        Invite invite = Invite.builder()
                .email(email)
                .role(role)
                .token(tokenGenerator.generate(tokenByteLength))
                .expirationDate(Instant.now().plus(validPeriod))
                .build();

        return inviteRepository.save(invite);
    }

    public Invite acceptInvite(String email, String token) {
        Invite invite = inviteRepository.findByEmailAndToken(email, token)
                .orElseThrow(() -> new InvalidInviteOperationException("No invite found for the provided email and token."));

        invite.accept();
        return invite;
    }

    public void submitInviteEmail(Invite invite) {
        invite.validateAllowedToSend();
        //TODO: add call to EmailSender to actually send the email
        invite.markAsSent();
    }
}
