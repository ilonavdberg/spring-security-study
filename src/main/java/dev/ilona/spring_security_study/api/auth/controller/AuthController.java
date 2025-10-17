package dev.ilona.spring_security_study.api.auth.controller;

import dev.ilona.spring_security_study.api.auth.request.LoginRequest;
import dev.ilona.spring_security_study.application.auth.LoginService;
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

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = loginService.execute(request);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
