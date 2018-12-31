package com.web.multifactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

public class MultifactorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultifactorApplication.class, args);
	}

}

