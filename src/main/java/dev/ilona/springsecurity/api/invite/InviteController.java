package dev.ilona.springsecurity.api.invite;

import dev.ilona.springsecurity.application.user.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invites")
public class InviteController {

    private final UserManagementService userManagementService;

    @PostMapping
    public void sendInvite(@RequestBody SendInviteRequest request) {
        userManagementService.createAndSendInviteForAdmin(request.email());
    }

    @PostMapping("/{token}/accept")
    public void acceptInvite() {
        userManagementService.createUserFromInvite();
    }
}
