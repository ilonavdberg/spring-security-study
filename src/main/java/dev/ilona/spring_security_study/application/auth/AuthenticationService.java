package dev.ilona.spring_security_study.application.auth;

import dev.ilona.spring_security_study.api.auth.LoginRequest;
import dev.ilona.spring_security_study.security.UserPrincipal;
import dev.ilona.spring_security_study.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return jwtService.generateToken(userPrincipal.getUser());
    }
}
