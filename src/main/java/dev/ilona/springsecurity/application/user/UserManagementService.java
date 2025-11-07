package dev.ilona.springsecurity.application.user;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserService userService;
    private final RoleService roleService;

    @Transactional
    public String registerUser(UserRegistrationRequest request) {
        Role role = roleService.getGeneralUserRole();
        User user = userService.createUser(request.username(), request.password(), request.email(), role);
        return user.getUsername();
    }
}
