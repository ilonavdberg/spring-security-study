package dev.ilona.springsecurity.application.auth;

import dev.ilona.springsecurity.api.auth.LoginRequest;
import dev.ilona.springsecurity.api.auth.RefreshResponse;
import dev.ilona.springsecurity.domain.auth.RefreshToken;
import dev.ilona.springsecurity.domain.auth.RefreshTokenRepository;
import dev.ilona.springsecurity.domain.auth.RefreshTokenService;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.security.UserPrincipal;
import dev.ilona.springsecurity.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return jwtService.generateToken(userPrincipal.getUser());
    }

    @Transactional
    public RefreshResponse refresh(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token."));

        try {
            if (!refreshToken.isValid()) {
                throw new BadCredentialsException("Refresh token expired.");
            }

            User user = refreshToken.getUser();
            RefreshToken newRefreshToken = refreshTokenService.createFor(user);
            String newAccessToken = jwtService.generateToken(user);
            return RefreshResponse.of(newRefreshToken.getToken(), newAccessToken);
        } finally {
            refreshTokenRepository.delete(refreshToken);
        }
    }
}

