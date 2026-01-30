package dev.ilona.springsecurity.domain.user.role;

import dev.ilona.springsecurity.exception.exceptions.DatabaseIntegrityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getGeneralUserRole() {
        return roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new DatabaseIntegrityException("Missing role in database: " + Role.RoleName.ROLE_USER));
    }

    public Role getAdminRole() {
        return roleRepository.findByName(Role.RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new DatabaseIntegrityException("Missing role in database: " + Role.RoleName.ROLE_ADMIN));
    }
}

