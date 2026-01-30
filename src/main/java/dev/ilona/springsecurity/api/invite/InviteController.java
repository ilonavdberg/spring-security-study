package dev.ilona.springsecurity.api.invite;

import dev.ilona.springsecurity.application.user.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invites")
public class InviteController {

    private final UserManagementService userManagementService;

    @PostMapping
    public void sendInvite(@Valid @RequestBody SendInviteRequest request) {
        userManagementService.createAndSendInviteForAdmin(request.email());
    }

    @PostMapping("/{token}/accept")
    public void acceptInvite(@PathVariable String token, @Valid @RequestBody AcceptInviteRequest request) {
        userManagementService.createUserFromInvite(request.email(), request.password(), token);
    }
}
