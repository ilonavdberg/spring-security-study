package dev.ilona.springsecurity.auth;

import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import dev.ilona.springsecurity.config.TestDataInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import({
        PostgresTestContainerConfig.class,
        TestDataInitializer.class
})
public class AuthenticationControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserManagementService userManagementService;

    @BeforeEach
    void setup() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "test-user",
                "test-user@email.com",
                "P@ssW0rd"
        );

        userManagementService.registerUser(request);
    }

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "test-user",
                            "password": "P@ssW0rd"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldRejectInvalidCredentials() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "test-user",
                            "password": "Wr0ng-P@ssW0rd"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }
}
