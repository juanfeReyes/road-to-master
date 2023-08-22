package com.r2m.apicomposer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApiComposerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiComposerApplication.class, args);
	}

}
