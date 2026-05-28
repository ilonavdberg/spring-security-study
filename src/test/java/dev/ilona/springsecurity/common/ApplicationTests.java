package dev.ilona.springsecurity.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(PostgresTestContainerConfig.class)
@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
