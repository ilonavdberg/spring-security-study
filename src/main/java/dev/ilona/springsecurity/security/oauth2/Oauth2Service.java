package dev.ilona.springsecurity.security.oauth2;

import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import dev.ilona.springsecurity.domain.user.UserService;
import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.domain.user.role.RoleService;
import dev.ilona.springsecurity.exception.exceptions.DuplicateEntryException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        Optional<User> existingUser = userRepository.findByUsernameAndDeletedFalse(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        if (userRepository.existsByEmailAndDeletedTrue(email)) {
            throw new DuplicateEntryException("This account has been deleted. Please contact support to recover your account.");
        }

        return userService.createUser(email, UserType.EXTERNAL, List.of(roleService.getStandardUserRole()));
    }
}
