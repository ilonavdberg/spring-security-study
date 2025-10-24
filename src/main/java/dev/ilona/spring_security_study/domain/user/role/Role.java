package dev.ilona.spring_security_study.domain.user.role;

import jakarta.persistence.*;
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

    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Getter
    public enum RoleName {
        GENERAL_USER("ROLE_USER"),
        ADMIN("ROLE_ADMIN");

        private final String value;

        RoleName(String value) {
            this.value = value;
        }
    }
}
