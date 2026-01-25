package dev.ilona.springsecurity.domain.auth;

import dev.ilona.springsecurity.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PACKAGE)
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Column(name = "token", nullable = false, unique = true, updatable = false)
    private String token;

    @NotBlank
    @Column(name = "expiration_date", nullable = false, updatable = false)
    private Instant expirationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Builder(access = AccessLevel.PACKAGE)
    public RefreshToken(String token, Instant expirationDate, User user) {
        setToken(token);
        setExpirationDate(expirationDate);
        setUser(user);
    }

    public boolean isValid() {
        return this.expirationDate.isAfter(Instant.now());
    }
}
