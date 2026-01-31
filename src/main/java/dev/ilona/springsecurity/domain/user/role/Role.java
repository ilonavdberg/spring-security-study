package dev.ilona.springsecurity.domain.user.role;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is a required field.")
    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    /**
     * Returns {@code true} if this role is internal (admins), {@code false} if this role is external (standard users).
     */
    public boolean isInternal() {
        return this.getName() == RoleName.ROLE_ADMIN;
    }

    @Getter
    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN
    }
}
