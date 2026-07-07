package dev.ilona.springsecurity.domain.user.invite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findByEmailAndToken(String email, String token);
    Optional<Invite> findByUuid(UUID uuid);
    Optional<Invite> findByEmailAndStatusIn(String email, Set<Invite.Status> statuses);

    default Optional<Invite> findActiveInviteByEmail(String email) {
        return findByEmailAndStatusIn(email, Invite.Status.activeStatuses());
    }
}
