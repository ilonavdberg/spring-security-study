package dev.ilona.springsecurity.domain.user;

import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String rawPassword, String email, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException((username));
        }
        //TODO: add check for email uniqueness

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .email(email)
                .role(role)
                .build();

        return userRepository.save(user);
    }
}
