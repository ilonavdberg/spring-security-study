package dev.ilona.spring_security_study.domain.user;

import dev.ilona.spring_security_study.domain.user.role.Role;
import dev.ilona.spring_security_study.domain.user.role.RoleService;
import dev.ilona.spring_security_study.exception.exceptions.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException((username));
        }

        Role generalUserRole = roleService.getGeneralUserRole();

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .role(generalUserRole)
                .build();

        return userRepository.save(user);
    }
}
