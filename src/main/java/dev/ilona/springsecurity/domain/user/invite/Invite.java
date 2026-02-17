package dev.ilona.springsecurity.domain.user.invite;

import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.IllegalStateTransitionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invites")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PACKAGE)
@Getter
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Status is a required field.")
    Status status;

    @NotBlank(message = "Token is a required field.")
    @Column(name = "token", nullable = false, unique = true, updatable = false)
    String token;

    @NotBlank(message = "Email is a required field.")
    @Column(name = "email", nullable = false, updatable = false)
    String email;

    @NotNull(message = "Expiration date is a required field.")
    @Column(name = "expiration_date", nullable = false, updatable = false)
    Instant expirationDate;

    @NotEmpty(message = "At least one role must be set.")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "invites_roles",
            joinColumns = @JoinColumn(name = "invite_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<Role> roles = new ArrayList<>();

    @Builder(access = AccessLevel.PACKAGE)
    public Invite(String token, String email, Instant expirationDate, @Singular List<Role> roles) {
        setToken(token);
        setEmail(email);
        setExpirationDate(expirationDate);
        setRoles(roles);
        setStatus(Status.NEW);
    }

    public void validateAllowedToSend() {
        if (this.status != Status.NEW) {
            throw new IllegalStateTransitionException("This invite is not in a valid state to be send.");
        }
    }

    public void markAsSent() {
        setStatus(Status.SENT);
    }

    public void accept() {
        if (this.expirationDate.isBefore(Instant.now())) {
            throw new IllegalStateTransitionException("This invite cannot be accepted because it has expired.");
        }

        if (this.status != Status.SENT) {
            throw new IllegalStateTransitionException("This invite is not in a valid state to be accepted.");
        }

        setStatus(Status.ACCEPTED);
    }


    private enum Status {
        NEW, SENT, ACCEPTED, REVOKED
    }
}
