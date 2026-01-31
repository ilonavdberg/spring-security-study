package dev.ilona.springsecurity.application.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteService;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserService userService;
    private final RoleService roleService;
    private final InviteService inviteService;

    @Transactional
    public UUID registerUser(UserRegistrationRequest request) {
        User user = userService.createUser(
                request.username(),
                request.password(),
                request.email(),
                roleService.getStandardUserRole()
        );
        return user.getUuid();
    }

    @Transactional
    public UUID createUserFromInvite(String email, String password, String token) {
        Invite invite = inviteService.acceptInvite(email, token);

        User user = userService.createUser(
                email, // Admins use their email address as their username
                password,
                email,
                invite.getRoles().toArray(new Role[0]) // convert List<Role> to Role[] for varargs
        );
        return user.getUuid();
    }

    @Transactional
    public void createAndSendInviteForAdmin(String email) {
        Invite invite = inviteService.createInvite(email, roleService.getAdminRole());
        inviteService.submitInviteEmail(invite);
    }
}
