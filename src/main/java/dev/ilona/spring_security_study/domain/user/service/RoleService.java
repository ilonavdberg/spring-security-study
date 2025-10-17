package dev.ilona.spring_security_study.domain.user.service;

import dev.ilona.spring_security_study.domain.user.model.Role;
import dev.ilona.spring_security_study.domain.user.repository.RoleRepository;
import dev.ilona.spring_security_study.exception.exceptions.RequiredRoleNotInDatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private static final String GENERAL_USER_ROLE = "ROLE_USER";

    public Role getGeneralUserRole() {
        return roleRepository.findByName(GENERAL_USER_ROLE)
                .orElseThrow(() -> new RequiredRoleNotInDatabaseException("Missing role in database: " + GENERAL_USER_ROLE));
    }
}

