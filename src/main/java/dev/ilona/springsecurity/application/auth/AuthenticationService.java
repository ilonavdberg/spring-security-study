package dev.ilona.springsecurity.application.auth;

import dev.ilona.springsecurity.api.auth.LoginRequest;
import dev.ilona.springsecurity.domain.user.refreshtoken.RefreshToken;
import dev.ilona.springsecurity.domain.user.refreshtoken.RefreshTokenRepository;
import dev.ilona.springsecurity.domain.user.refreshtoken.RefreshTokenService;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.security.UserPrincipal;
import dev.ilona.springsecurity.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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

    public TokenPair login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        return createTokenPair(user);
    }

    @Transactional
    public TokenPair refresh(String token) {
        RefreshToken refreshToken = refreshTokenService.resolve(token);

        TokenPair tokenPair = createTokenPair(refreshToken.getUser());
        refreshTokenRepository.delete(refreshToken); // Refresh tokens are single-use

        return tokenPair;
    }

    private TokenPair createTokenPair(User user) {
        String refreshToken = refreshTokenService.createFor(user).getToken();
        String accessToken = jwtService.generateToken(user);
        return TokenPair.of(refreshToken, accessToken);
    }
}

