package dev.ilona.springsecurity.domain.user;

import dev.ilona.springsecurity.domain.user.refreshtoken.RefreshToken;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.IllegalStateTransitionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PACKAGE)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull(message = "UUID is a required field.")
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @NotBlank(message = "Username is a required field.")
    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @NotBlank(message = "Email address is a required field.")
    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @NotEmpty(message = "A user should have at least one role.")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @NotBlank(message = "Authentication method is a required field.")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthenticationMethod authenticationMethod;

    @NotNull(message = "Status is a required field.")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @Builder(access = AccessLevel.PACKAGE)
    public User(AuthenticationMethod authenticationMethod, String username, String password, String email, List<Role> roles) {
        setAuthenticationMethod(authenticationMethod);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setRoles(roles);
        setStatus(Status.ACTIVE);
    }

    public void block() {
        if (status == Status.BLOCKED) {
            throw new IllegalStateTransitionException("User is already blocked.");
        }

        if (isInternal()) {
            throw new IllegalStateTransitionException("Internal users cannot be blocked.");
        }

        setStatus(Status.BLOCKED);
    }

    public boolean isInternal() {
        return roles.getFirst().isInternal(); // safe because roles are homogeneous
    }

    public enum Status {
        ACTIVE,
        BLOCKED
    }
}
