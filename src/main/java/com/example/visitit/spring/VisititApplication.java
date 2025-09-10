package com.example.visitit.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.visitit.spring")
public class VisititApplication {
	public static void main(String[] args) {
		SpringApplication.run(VisititApplication.class, args);
	}
}
