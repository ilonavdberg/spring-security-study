package dev.ilona.springsecurity.domain.user;

import dev.ilona.springsecurity.domain.user.refreshtoken.RefreshToken;
import dev.ilona.springsecurity.domain.user.role.Role;
import dev.ilona.springsecurity.exception.exceptions.IllegalStateTransitionException;
import dev.ilona.springsecurity.exception.exceptions.PolicyViolationException;
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

    @NotNull(message = "Type of user must be specified.")
    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

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

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Builder(access = AccessLevel.PACKAGE)
    public User(AuthenticationMethod authenticationMethod, String username, String password, String email, UserType userType, List<Role> roles) {
        setAuthenticationMethod(authenticationMethod);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setUserType(userType);
        setRoles(roles);
        setStatus(Status.ACTIVE);

        ensureCompatibleRoles();
    }

    public void block() {
        if (isBlocked()) {
            throw new IllegalStateTransitionException("User is already blocked.");
        }

        if (userType.isInternal()) {
            throw new IllegalStateTransitionException("Internal users cannot be blocked.");
        }

        setStatus(Status.BLOCKED);
    }

    public boolean isBlocked() {
        return status == Status.BLOCKED;
    }

    private void ensureCompatibleRoles() {
        for (Role role : roles) {
            if (!role.isCompatibleWith(userType)) {
                throw new PolicyViolationException("Role '" + role.getName() + "' is not compatible with user type " + userType);
            }
        }
    }

    public enum Status {
        ACTIVE,
        BLOCKED
    }
}
