package dev.ilona.spring_security_study.domain.user.role;

import dev.ilona.spring_security_study.exception.exceptions.RequiredRoleNotInDatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getGeneralUserRole() {
        return roleRepository.findByName(Role.RoleName.GENERAL_USER)
                .orElseThrow(() -> new RequiredRoleNotInDatabaseException("Missing role in database: " + Role.RoleName.GENERAL_USER));
    }
}

