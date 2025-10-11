package dev.ilona.spring_security_study.domain.user.repository;

import dev.ilona.spring_security_study.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
