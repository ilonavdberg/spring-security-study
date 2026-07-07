package dev.ilona.springsecurity.domain.user.invite;

import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.domain.user.policies.EmailPolicy;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.DuplicateEntryException;
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
    private final EmailPolicy emailPolicy;

    @Value("${security.invite.token.valid-period}")
    private Duration validPeriod;

    @Value("${security.invite.token.byte-length}")
    private int tokenByteLength;

    public Invite create(String email, Role role) {
        if (inviteRepository.findActiveInviteByEmail(email).isPresent()) {
            throw new DuplicateEntryException("An invite already exists for email: %s.".formatted(email));
        }

        emailPolicy.validate(email, UserType.INTERNAL);

        Invite invite = Invite.builder()
                .email(email)
                .role(role)
                .token(tokenGenerator.generate(tokenByteLength))
                .expirationDate(Instant.now().plus(validPeriod))
                .build();

        return inviteRepository.save(invite);
    }
}
