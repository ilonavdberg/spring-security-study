package dev.ilona.springsecurity.api.invite;

import dev.ilona.springsecurity.application.user.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invites")
public class InviteController {

    private final UserManagementService userManagementService;

    @PostMapping
    public ResponseEntity<Void> sendInvite(@Valid @RequestBody SendInviteRequest request) {
        userManagementService.createAndSendInviteForAdmin(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{token}/accept")
    public ResponseEntity<Void> acceptInvite(@PathVariable String token, @Valid @RequestBody AcceptInviteRequest request) {
        UUID uuid = userManagementService.createUserFromInvite(request.email(), request.password(), token);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/users/{uuid}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
