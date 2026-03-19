package dev.ilona.springsecurity.application.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteRepository;
import dev.ilona.springsecurity.domain.user.invite.InviteService;
import dev.ilona.springsecurity.domain.user.refreshtoken.RefreshTokenRepository;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserManagementService {

    private final UserService userService;
    private final RoleService roleService;
    private final InviteService inviteService;
    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public UUID registerUser(UserRegistrationRequest request) {
        User user = userService.createUser(
                request.username(),
                request.password(),
                request.email(),
                UserType.EXTERNAL,
                List.of(roleService.getStandardUserRole())
        );
        return user.getUuid();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void blockUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("No user found with uuid: " + uuid));

        user.block();
        refreshTokenRepository.deleteAllByUser(user);
    }

    public UUID createUserFromInvite(String email, String password, String token) {
        Invite invite = inviteRepository.findByEmailAndToken(email, token)
                .orElseThrow(() -> new EntityNotFoundException("No invite found for the provided token and email combination."));

        invite.accept();

        User user = userService.createUser(
                email, // Internal users use their email address as their username
                password,
                email,
                UserType.INTERNAL,
                invite.getRoles()
        );
        return user.getUuid();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createAndSendInviteForAdmin(String email) {
        Invite invite = inviteService.createInvite(email, roleService.getAdminRole());

        invite.validateAllowedToSend();
        //TODO: add call to EmailSender to actually send the email
        invite.markAsSent();
    }
}
