package dev.ilona.spring_security_study.api.auth.controller;

import dev.ilona.spring_security_study.api.auth.request.LoginRequest;
import dev.ilona.spring_security_study.application.auth.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        loginService.execute(request);
        return null; //TODO: return JWT token in uri?
    }
}
