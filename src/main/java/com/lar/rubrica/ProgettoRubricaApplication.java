package com.lar.rubrica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ProgettoRubricaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgettoRubricaApplication.class, args);
	}
}