package dev.ilona.springsecurity.security.jwt;

import dev.ilona.springsecurity.security.DatabaseUserDetailsService;
import dev.ilona.springsecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
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

        String username = jwtService.extractUsername(token);
        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        return new JwtAuthenticationToken(principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
