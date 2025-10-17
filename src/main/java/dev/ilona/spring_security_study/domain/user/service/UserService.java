package dev.ilona.spring_security_study.domain.user.service;

import dev.ilona.spring_security_study.api.auth.request.UserRegistrationRequest;
import dev.ilona.spring_security_study.domain.user.model.Role;
import dev.ilona.spring_security_study.domain.user.model.User;
import dev.ilona.spring_security_study.domain.user.repository.UserRepository;
import dev.ilona.spring_security_study.exception.exceptions.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException((request.username()));
        }

        User user = User.builder()
                .username(request.username())
                .password(request.password())
                .passwordEncoder(passwordEncoder)
                .role(roleService.getGeneralUserRole())
                .build();

        userRepository.save(user);
    }
}
