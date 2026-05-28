package dev.ilona.springsecurity.common;

import dev.ilona.springsecurity.domain.user.UserType;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.domain.user.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestDataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByName(Role.RoleName.ROLE_USER)) {
                Role role = new Role();
                role.setName(Role.RoleName.ROLE_USER);
                role.setRestrictedUserType(UserType.EXTERNAL);

                roleRepository.save(role);
            }

            if (!roleRepository.existsByName(Role.RoleName.ROLE_ADMIN)) {
                Role role = new Role();
                role.setName(Role.RoleName.ROLE_ADMIN);
                role.setRestrictedUserType(UserType.INTERNAL);

                roleRepository.save(role);
            }
        };
    }

}
