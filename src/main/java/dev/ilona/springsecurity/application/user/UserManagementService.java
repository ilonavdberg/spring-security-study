package dev.ilona.springsecurity.application.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.invite.Invite;
import dev.ilona.springsecurity.domain.user.invite.InviteRepository;
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
    private final InviteRepository inviteRepository;

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
    public void createUserFromInvite(String email, String password, String token) {
        Invite invite = inviteService.resolveInvite(email, token);
        invite.accept();

        //TODO: create Admin User

    }

    @Transactional
    public void createAndSendInviteForAdmin(String email) {
        Invite invite = inviteService.creatInvite(email, roleService.getAdminRole());
        //TODO: add logic to send invite via EmailSender and update Invite status
    }
}
