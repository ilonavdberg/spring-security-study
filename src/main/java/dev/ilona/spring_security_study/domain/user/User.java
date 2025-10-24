package dev.ilona.spring_security_study.domain.user;

import dev.ilona.spring_security_study.domain.user.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter(AccessLevel.PACKAGE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    @NotBlank(message = "Username must not be blank.")
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password must not be blank.")
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
    public User(String username, String password, @Singular Set<Role> roles) {
        setUsername(username);
        setPassword(password);
        setRoles(roles);
    }
}
