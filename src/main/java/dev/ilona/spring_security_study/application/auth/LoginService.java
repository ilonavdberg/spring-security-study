package dev.ilona.spring_security_study.application.auth;

import dev.ilona.spring_security_study.api.auth.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;

    @Transactional
    public void perform(LoginRequest loginRequest) {
        //TODO: add login logic incorporating authenticationManager (see ChatGPT chat)
        //return JWT token?
    }
}
