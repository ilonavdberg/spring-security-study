package dev.ilona.springsecurity.api.auth;

import dev.ilona.springsecurity.application.auth.AuthenticationService;
import dev.ilona.springsecurity.application.auth.TokenPair;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(@Valid @RequestBody LoginRequest request) {
        TokenPair tokenPair = authenticationService.login(request);
        return ResponseEntity.ok(tokenPair);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@Valid @RequestBody RefreshRequest request) {
        TokenPair tokenPair = authenticationService.refresh(request.token());
        return ResponseEntity.ok(tokenPair);
    }
}
