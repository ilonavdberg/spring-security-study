package dev.ilona.springsecurity.api.user;

import dev.ilona.springsecurity.application.user.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegistrationRequest request) {
        UUID uuid = userManagementService.registerUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/users/{uuid}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
