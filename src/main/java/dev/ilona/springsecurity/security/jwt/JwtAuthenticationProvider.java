package dev.ilona.springsecurity.security.jwt;

import dev.ilona.springsecurity.security.DatabaseUserDetailsService;
import dev.ilona.springsecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final DatabaseUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) authentication;
        String token = jwt.getCredentials().toString();

        if (!jwtService.validateToken(token)) {
            throw new BadCredentialsException("Invalid JWT token");
        }

        String username = jwtService.extractSubject(token);
        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        if (!principal.isAccountNonLocked()) {
            throw new LockedException("Account is locked");
        }

        if (!principal.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }

        if (!principal.isAccountNonExpired()) {
            throw new AccountExpiredException("Account expired");
        }

        if (!principal.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Credentials expired");
        }

        return new JwtAuthenticationToken(principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
