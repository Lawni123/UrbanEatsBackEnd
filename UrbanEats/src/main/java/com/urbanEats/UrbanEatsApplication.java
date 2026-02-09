package com.urbanEats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class UrbanEatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrbanEatsApplication.class, args);
	}

}
