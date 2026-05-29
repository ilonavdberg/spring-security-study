package dev.ilona.springsecurity.common;

import dev.ilona.springsecurity.config.PostgresTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(PostgresTestContainerConfig.class)
@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
