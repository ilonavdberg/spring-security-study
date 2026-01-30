package dev.ilona.springsecurity.domain.user.invite;

import dev.ilona.springsecurity.domain.user.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invites")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Token must be provided.")
    @Column(name = "token", nullable = false, unique = true, updatable = false)
    String token;

    @NotBlank(message = "Email must be provided.")
    @Column(name = "email", nullable = false, updatable = false)
    String email;

    @NotNull(message = "Expiration date must be provided.")
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


}
