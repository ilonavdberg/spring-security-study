package dev.ilona.spring_security_study.domain.user.repository;

import dev.ilona.spring_security_study.domain.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
