package dev.ilona.spring_security_study;

import org.springframework.boot.SpringApplication;

public class TestSpringSecurityStudyApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
