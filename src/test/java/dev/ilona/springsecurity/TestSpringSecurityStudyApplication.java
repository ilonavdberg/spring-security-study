package dev.ilona.springsecurity;

import org.springframework.boot.SpringApplication;

public class TestSpringSecurityStudyApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
