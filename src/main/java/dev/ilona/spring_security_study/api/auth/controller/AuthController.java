package dev.ilona.spring_security_study.api.auth.controller;

import dev.ilona.spring_security_study.api.auth.request.LoginRequest;
import dev.ilona.spring_security_study.api.auth.request.UserRegistrationRequest;
import dev.ilona.spring_security_study.domain.user.service.AuthenticationService;
import dev.ilona.spring_security_study.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = authenticationService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
