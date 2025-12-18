package com.resume.resumematching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ResumematchingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumematchingApplication.class, args);
	}

}
