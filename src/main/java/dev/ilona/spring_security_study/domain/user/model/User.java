package dev.ilona.spring_security_study.domain.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Builder
    public User(String username, String password, @Singular Set<Role> roles, PasswordEncoder passwordEncoder) {
        setUsername(username);
        encodeAndSetPassword(password, passwordEncoder);
        setRoles(roles);
    }

    public void setUsername(String username) {
        this.username = Objects.requireNonNull(username, "username must not be null");
    }

    public void encodeAndSetPassword(String password, PasswordEncoder passwordEncoder) {
        Objects.requireNonNull(passwordEncoder, "password encoder must be set for encoding the password.");
        this.password = passwordEncoder.encode(Objects.requireNonNull(password, "password must not be null"));
    }

    public void setRoles(Set<Role> roles) {
        if (roles != null && !roles.isEmpty()) {
            this.roles.addAll(roles);
        }

        if (this.roles.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be set.");
        }
    }
}
