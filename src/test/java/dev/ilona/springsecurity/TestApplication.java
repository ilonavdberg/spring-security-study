package dev.ilona.springsecurity;

import dev.ilona.springsecurity.common.PostgresTestContainerConfig;
import org.springframework.boot.SpringApplication;

public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(PostgresTestContainerConfig.class).run(args);
	}

}
