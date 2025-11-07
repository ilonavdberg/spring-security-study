package dev.ilona.springsecurity.security.oauth2;

import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Oauth2Service {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository;

    public User retrieveOrCreateUser(DefaultOAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");

        Role role = roleService.getGeneralUserRole();

        User user = userRepository.findByEmail(email)
                // Use email as username because it must be unique
                // Assign a random password since OAuth2 users don’t log in with a password
                .orElseGet(() -> userService.createUser(email, UUID.randomUUID().toString(), email, role));
        return user;
    }
}
