package dev.ilona.springsecurity.security.oauth2;

import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Oauth2Service {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository;

    public User retrieveOrCreateUser(DefaultOAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("OAuth2 provider did not supply an email");
        }

        Role role = roleService.getGeneralUserRole();

        return userRepository.findByEmail(email)
                .orElseGet(() -> userService.createUser(email, role));
    }
}
