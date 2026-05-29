package dev.ilona.springsecurity.user;

import dev.ilona.springsecurity.api.user.UserController;
import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.domain.user.policies.PasswordPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegisterUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserManagementService userManagementService;

    @Test
    void shouldReturn201AndLocationHeader() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userManagementService.registerExternalUser(any()))
                .thenReturn(uuid);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "john_doe",
                            "email": "j.doe@email.com",
                            "password": "P@ssW0rd"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/api/users/" + uuid
                ));
    }

    @ParameterizedTest
    @MethodSource("invalidUsernames")
    void shouldReturn400WhenUsernameIsInvalid(String username) throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "%s",
                            "email": "j.doe@email.com",
                            "password": "P@ssW0rd"
                        }
                        """.formatted(username)))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> invalidUsernames() {
        return Stream.of(
                "",
                "abcdefg",
                "a".repeat(33)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    void shouldReturn400WhenEmailIsInvalid(String email) throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "john_doe",
                            "email": "%s",
                            "password": "P@ssW0rd"
                        }
                        """.formatted(email)))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> invalidEmails() {
        return Stream.of(
                "",
                "text",
                "@domain.com",
                "user@"
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPasswords")
    void shouldReturn400WhenPasswordIsInvalid(String password) throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "john_doe",
                            "email": "j.doe@email.com",
                            "password": "%s"
                        }
                        """.formatted(password)))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> invalidPasswords() {
        return Stream.of(
                "",
                "a".repeat(PasswordPolicy.MIN_LENGTH - 3) + "A1!",
                "a".repeat(PasswordPolicy.MAX_LENGTH - 3) + "A1!",
                "missing_uppercase1!",
                "MISSING_LOWERCASE1!",
                "NoNumber!",
                "NoSpecialChar1"
        );
    }

}
