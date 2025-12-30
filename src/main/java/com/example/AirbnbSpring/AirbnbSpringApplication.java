package com.example.AirbnbSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirbnbSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirbnbSpringApplication.class, args);
	}

}
