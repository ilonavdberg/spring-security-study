package dev.ilona.springsecurity.application.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteService;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserService userService;
    private final RoleService roleService;
    private final InviteService inviteService;
    private final UserRepository userRepository;

    @Transactional
    public UUID registerUser(UserRegistrationRequest request) {
        User user = userService.createUser(
                request.username(),
                request.password(),
                request.email(),
                List.of(roleService.getStandardUserRole())
        );
        return user.getUuid();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void blockUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("No user found with uuid: " + uuid));

        user.block();
    }

    @Transactional
    public UUID createUserFromInvite(String email, String password, String token) {
        Invite invite = inviteService.acceptInvite(email, token);

        User user = userService.createUser(
                email, // Admins use their email address as their username
                password,
                email,
                invite.getRoles()
        );
        return user.getUuid();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void createAndSendInviteForAdmin(String email) {
        Invite invite = inviteService.createInvite(email, roleService.getAdminRole());
        inviteService.submitInviteEmail(invite);
    }
}
