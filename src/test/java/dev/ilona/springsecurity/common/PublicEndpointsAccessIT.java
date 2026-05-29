package dev.ilona.springsecurity.common;

import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Security smoke test suite for public API endpoints.
 *
 * <p>This test class verifies only whether endpoints are accessible through the
 * Spring Security filter chain without authentication.</p>
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestContainerConfig.class)
public class PublicEndpointsAccessIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowAnonymousUserToRegister() throws Exception {
        mockMvc.perform(post("/api/users/register"))
                .andExpect(isAccessibleWithoutAuthentication());
    }

    @Test
    void shouldAllowAnonymousUserToLogin() throws Exception {
        mockMvc.perform(post("/auth/login"))
                .andExpect(isAccessibleWithoutAuthentication());
    }

    @Test
    void shouldAllowAnonymousUserToRefreshToken() throws Exception {
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
