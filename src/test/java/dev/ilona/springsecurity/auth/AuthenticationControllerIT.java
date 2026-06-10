package dev.ilona.springsecurity.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ilona.springsecurity.api.user.UserRegistrationRequest;
import dev.ilona.springsecurity.application.auth.TokenPair;
import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import dev.ilona.springsecurity.config.TestDataInitializer;
import dev.ilona.springsecurity.domain.user.User;
import dev.ilona.springsecurity.domain.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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
    ObjectMapper objectMapper;

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    UserRepository userRepository;

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
    void shouldReturnTokenPairWhenLoginCredentialsAreValid() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "test-user",
                            "password": "P@ssW0rd"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void shouldRejectWhenLoginCredentialsAreInvalid() throws Exception {
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

    @Test
    void shouldRejectLoginByBlockedUser() throws Exception {
        User user = userRepository.findByUsernameAndDeletedFalse("test-user")
                .orElseThrow();

        user.block();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "test-user",
                            "password": "P@ssW0rd"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectLoginByDeletedUser() throws Exception {
        User user = userRepository.findByUsernameAndDeletedFalse("test-user")
                .orElseThrow();

        user.delete();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "test-user",
                            "password": "P@ssW0rd"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnTokenPairWhenRefreshTokenIsValid() throws Exception {
        TokenPair tokenPair = loginAndGetTokenPair();

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "%s"
                        }
                        """.formatted(tokenPair.refreshToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void shouldRejectWhenRefreshTokenIsAlreadyUsed() throws Exception {
        TokenPair tokenPair = loginAndGetTokenPair();

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "%s"
                        }
                        """.formatted(tokenPair.refreshToken())))
                .andReturn();

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "%s"
                        }
                        """.formatted(tokenPair.refreshToken())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectWhenRefreshTokenIsInvalid() throws Exception {
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "this-is-an-invalid-refresh-token"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectWhenRefreshTokenIsExpired() throws Exception {
        TokenPair tokenPair = loginAndGetTokenPair();

        entityManager.createNativeQuery("""
                            UPDATE refresh_tokens
                            SET expiration_date = :expired_at
                            WHERE token = :token
                        """)
                .setParameter("expired_at", Instant.now().minusSeconds(1))
                .setParameter("token", tokenPair.refreshToken())
                .executeUpdate();

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "%s"
                        }
                        """.formatted(tokenPair.refreshToken())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectTokenRefreshByBlockedUser() throws Exception {
        TokenPair tokenPair = loginAndGetTokenPair();

        User user = userRepository.findByUsernameAndDeletedFalse("test-user")
                .orElseThrow();

        user.block();

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "%s"
                        }
                        """.formatted(tokenPair.refreshToken())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectTokenRefreshByDeletedUser() throws Exception {
        TokenPair tokenPair = loginAndGetTokenPair();

        User user = userRepository.findByUsernameAndDeletedFalse("test-user")
                .orElseThrow();

        user.delete();

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "token": "%s"
                        }
                        """.formatted(tokenPair.refreshToken())))
                .andExpect(status().isUnauthorized());
    }

    private TokenPair loginAndGetTokenPair() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "test-user",
                            "password": "P@ssW0rd"
                        }
                        """))
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), TokenPair.class);
    }

}
