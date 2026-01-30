package dev.ilona.springsecurity.domain.user.invite;

import dev.ilona.springsecurity.domain.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;

    public Invite creatInvite(String email, Role role) {
        Invite invite = Invite.builder()
                .email(email)
                .role(role)
                .token(null) //TODO: generate token and inject here
                .expirationDate(null) //TODO: calculate expiration date and inject here
                .build();

        return inviteRepository.save(invite);
    }
}
