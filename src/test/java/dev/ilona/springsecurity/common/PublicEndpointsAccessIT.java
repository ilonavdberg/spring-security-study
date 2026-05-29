package dev.ilona.springsecurity.common;

import dev.ilona.springsecurity.api.auth.RefreshResponse;
import dev.ilona.springsecurity.application.auth.AuthenticationService;
import dev.ilona.springsecurity.application.user.UserManagementService;
import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestContainerConfig.class)
public class PublicEndpointsAccessIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserManagementService userManagementService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    void shouldAllowAnonymousUserToRegister() throws Exception {
        when(userManagementService.registerExternalUser(any()))
                .thenReturn(UUID.randomUUID());

        mockMvc.perform(post("/api/users/register"))
                .andExpect(isAccessibleWithoutAuthentication());
    }

    @Test
    void shouldAllowAnonymousUserToLogin() throws Exception {
        when(authenticationService.login(any()))
                .thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login"))
                .andExpect(isAccessibleWithoutAuthentication());
    }

    @Test
    void shouldAllowAnonymousUserToRefreshToken() throws Exception {
        when(authenticationService.refresh(any()))
                .thenReturn(RefreshResponse.of(
                        "new-fake-refresh-token",
                        "new-fake-jwt-token"
                ));

        mockMvc.perform(post("/auth/refresh"))
                .andExpect(isAccessibleWithoutAuthentication());
    }


    private ResultMatcher isAccessibleWithoutAuthentication() {
        return result ->
                assertThat(result.getResponse().getStatus()).isNotIn(
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.UNAUTHORIZED.value()
                );
    }
}
