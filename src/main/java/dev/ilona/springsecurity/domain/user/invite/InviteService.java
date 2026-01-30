package dev.ilona.springsecurity.domain.user.invite;

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
    private final TokenGenerator tokenGenerator;

    @Value("${security.invite.token.valid-period}")
    private final Duration validPeriod;

    @Value("${security.invite.token.byte-length}")
    private final int tokenByteLength;

    public Invite creatInvite(String email, Role role) {
        //TODO: add unique email check

        Invite invite = Invite.builder()
                .email(email)
                .role(role)
                .token(tokenGenerator.generate(tokenByteLength))
                .expirationDate(Instant.now().plus(validPeriod))
                .build();

        return inviteRepository.save(invite);
    }

    public Invite resolveInvite(String email, String token) {
        return inviteRepository.findByEmailAndToken(email, token)
                .orElseThrow(() -> new InvalidInviteOperationException("No invite found for the provided email and token."));
    }
}
