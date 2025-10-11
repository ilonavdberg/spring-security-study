package dev.ilona.spring_security_study.domain.user.service;

import dev.ilona.spring_security_study.domain.user.model.User;
import dev.ilona.spring_security_study.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//    @PostConstruct
    public void addUser() {
        User user = User.builder()
                .username("test_user")
                .password("password")
                .build();

        userRepository.save(user);
    }
}
