package dev.ilona.spring_security_study.domain.user;

import dev.ilona.spring_security_study.domain.user.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PACKAGE)
@Getter
public class User {

    public static class Constraints {
        public static final String USERNAME_PATTERN_REGEXP = "\\S{8,}";
        public static final String USERNAME_PATTERN_MESSAGE = "Username must be at least 8 characters long and must not contain whitespace.";

        //Note: these validation constraints apply to the raw password; in the entity the encoded password will be stored
        public static final String PASSWORD_PATTERN_REGEXP = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        public static final String PASSWORD_PATTERN_MESSAGE = "Password must be at least 8 characters and contain at least one lowercase letter, one uppercase letter, one number and one special character.";

        public static final String ROLES_NOT_EMPTY_MESSAGE = "At least one role must be set.";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    @Pattern(
            regexp = User.Constraints.USERNAME_PATTERN_REGEXP,
            message = User.Constraints.USERNAME_PATTERN_MESSAGE
    )
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password must be provided.")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @NotEmpty(message = Constraints.ROLES_NOT_EMPTY_MESSAGE)
    private Set<Role> roles = new HashSet<>();

    @Builder(access = AccessLevel.PACKAGE)
    public User(String username, String password, @Singular Set<Role> roles) {
        setUsername(username);
        setPassword(password);
        setRoles(roles);
    }
}
