package dev.ilona.spring_security_study.data;

import dev.ilona.spring_security_study.domain.user.UserService;
import dev.ilona.spring_security_study.domain.user.role.Role;
import dev.ilona.spring_security_study.domain.user.User;
import dev.ilona.spring_security_study.domain.user.role.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Temporary data initializer for quickly populating the database with example records.
 * <p>
 * This class is used to create sample users and roles during development.
 * It is intended as a short-term solution and will eventually be replaced
 * by proper database migration scripts (e.g., Flyway). When migrations are in place,
 * Hibernate's automatic schema update will be disabled.
 * </p>
 * <p>
 * Note: Uncomment {@code @PostConstruct} if you want the initialization to execute.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserService userService;
    private final RoleRepository roleRepository;

//    @PostConstruct
    public void addUser() {
        User user = userService.createUser("test_user", "password");
    }

//    @PostConstruct
    public void addRole() {
        Role role = new Role();
        role.setName(Role.RoleName.ADMIN);
        roleRepository.save(role);
    }
}
