package dev.ilona.springsecurity.domain.user.role;

import dev.ilona.springsecurity.domain.user.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Setter
@Getter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is a required field.")
    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @NotNull(message = "Role must specify which user type it is restricted to.")
    @Column(name = "restricted_to", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType restrictedUserType;

    /**
     * Returns {@code true} if this role is internal (admins), {@code false} if this role is external (standard users).
     */
    @Deprecated
    public boolean isInternal() {
        return this.getName() == RoleName.ROLE_ADMIN;
    }

    public boolean isCompatibleWith(UserType userType) {
        return this.restrictedUserType == userType;
    }

    @Getter
    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN
    }
}
