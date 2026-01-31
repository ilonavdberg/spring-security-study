package dev.ilona.springsecurity.domain.user;

import dev.ilona.springsecurity.domain.user.refreshtoken.RefreshToken;
import dev.ilona.springsecurity.domain.user.role.Role;
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

    @NotEmpty(message = "At least one role must be set.")
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

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @Builder(access = AccessLevel.PACKAGE)
    public User(AuthenticationMethod authenticationMethod, String username, String password, String email, List<Role> roles) {
        setAuthenticationMethod(authenticationMethod);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setRoles(roles);
    }
}
