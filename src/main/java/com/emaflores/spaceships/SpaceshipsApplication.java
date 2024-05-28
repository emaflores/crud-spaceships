package com.emaflores.spaceships;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport
public class SpaceshipsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpaceshipsApplication.class, args);
	}
}

