package com.codeuai.smarttaskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmartTaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartTaskManagerApplication.class, args);
	}

}
