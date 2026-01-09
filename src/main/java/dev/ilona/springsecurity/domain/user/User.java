package dev.ilona.springsecurity.domain.user;

import dev.ilona.springsecurity.domain.user.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PACKAGE)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    @NotBlank(message = "Username must be provided.")
    private String username;

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    @NotBlank(message = "Email address must be provided.")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password must be provided.")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @NotEmpty(message = "At least one role must be set.")
    private Set<Role> roles = new HashSet<>();

    @Builder(access = AccessLevel.PACKAGE)
    public User(String username, String password, String email, @Singular Set<Role> roles) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setRoles(roles);
    }
}
