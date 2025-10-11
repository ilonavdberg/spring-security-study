package dev.ilona.springsecurity.domain.user.role;

import dev.ilona.springsecurity.exception.exceptions.RequiredRoleNotInDatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getGeneralUserRole() {
        return roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new RequiredRoleNotInDatabaseException("Missing role in database: " + Role.RoleName.ROLE_USER));
    }
}

