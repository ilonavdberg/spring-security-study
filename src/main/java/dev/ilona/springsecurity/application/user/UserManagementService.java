package dev.ilona.springsecurity.application.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteService;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserService userService;
    private final RoleService roleService;
    private final InviteService inviteService;

    @Transactional
    public String registerUser(UserRegistrationRequest request) {
        User user = userService.createUser(
                request.username(),
                request.password(),
                request.email(),
                roleService.getGeneralUserRole()
        );
        return user.getUsername();
    }

    @Transactional
    public void createUserFromInvite() {
        //TODO: create Admin User
        //TODO: update Invite status
    }

    @Transactional
    public void createAndSendInviteForAdmin(String email) {
        Invite invite = inviteService.creatInvite(email, roleService.getAdminRole());
        //TODO: add logic to send invite via EmailSender and update Invite status
    }
}
