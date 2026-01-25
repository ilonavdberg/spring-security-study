package dev.ilona.springsecurity.domain.user;

import dev.ilona.springsecurity.domain.auth.RefreshToken;
import dev.ilona.springsecurity.domain.user.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @NotBlank(message = "Username must be provided.")
    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @NotBlank(message = "Email address must be provided.")
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
    private Set<Role> roles = new HashSet<>();

    @NotBlank(message = "Authentication method must be provided.")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthenticationMethod authenticationMethod;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @Builder(access = AccessLevel.PACKAGE)
    public User(AuthenticationMethod authenticationMethod, String username, String password, String email, @Singular Set<Role> roles) {
        setAuthenticationMethod(authenticationMethod);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setRoles(roles);
    }
}
