package dev.ilona.spring_security_study.data;

import dev.ilona.spring_security_study.domain.user.model.Role;
import dev.ilona.spring_security_study.domain.user.model.User;
import dev.ilona.spring_security_study.domain.user.repository.RoleRepository;
import dev.ilona.spring_security_study.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    //    @PostConstruct
    public void addUser() {
        User user = User.builder()
                .username("test_user")
                .password("password", passwordEncoder)
                .build();

        userRepository.save(user);
    }

//    @PostConstruct
    public void addRole() {
        Role role = new Role();
        role.setName("ROLE_USER");

        roleRepository.save(role);
    }
}
