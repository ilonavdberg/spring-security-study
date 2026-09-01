package dev.ilona.springsecurity.invite;

import dev.ilona.springsecurity.api.invite.InviteController;
import dev.ilona.springsecurity.application.user.InviteManagementService;
import dev.ilona.springsecurity.application.user.UserManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InviteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InviteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    InviteManagementService inviteManagementService;

    @MockitoBean
    UserManagementService userManagementService;

    @Test
    void shouldReturn201CreatedWhenEmailIsValid() throws Exception {
        String email = "test@email.com";
        UUID uuid = UUID.randomUUID();
        when(inviteManagementService.createInviteForAdminUser(email))
                .thenReturn(uuid);

        mockMvc.perform(post("/api/invites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "%s"
                        }
                        """.formatted(email)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/api/invites/" + uuid
                ));
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    void shouldReturn400WhenEmailFormatIsInvalid(String email) throws Exception {
        mockMvc.perform(post("/api/invites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": %s
                        }
                        """.formatted(email)))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> invalidEmails() {
        return Stream.of(
                "",
                "text",
                "@domain.com",
                "user@",
                "user@@domain.com",
                null
        );
    }
}
