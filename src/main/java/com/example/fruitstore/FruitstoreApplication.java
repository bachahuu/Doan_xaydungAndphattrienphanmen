package com.example.fruitstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FruitstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FruitstoreApplication.class, args);
	}

}
