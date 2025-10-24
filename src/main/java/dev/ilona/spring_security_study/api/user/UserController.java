package dev.ilona.spring_security_study.api.user;

import dev.ilona.spring_security_study.application.user.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegistrationRequest request) {
        String username = userManagementService.registerUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{username}")
                .buildAndExpand(username)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
