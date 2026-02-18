package dev.ilona.springsecurity.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndDeletedFalse(String username);
    Optional<User> findByEmailAndDeletedFalse(String email);
    Optional<User> findByUuid(UUID uuid);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndDeletedTrue(String email);
}
