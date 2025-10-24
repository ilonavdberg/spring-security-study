package dev.ilona.spring_security_study.application.user;

import dev.ilona.spring_security_study.api.auth.request.UserRegistrationRequest;
import dev.ilona.spring_security_study.domain.user.User;
import dev.ilona.spring_security_study.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserService userService;

    @Transactional
    public String registerUser(UserRegistrationRequest request) {
        User user = userService.createUser(request.username(), request.password());
        return user.getUsername();
    }
}
