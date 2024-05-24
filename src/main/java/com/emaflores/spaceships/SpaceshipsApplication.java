package com.emaflores.spaceships;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpaceshipsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpaceshipsApplication.class, args);
	}
}

