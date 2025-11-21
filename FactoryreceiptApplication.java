package com.factoryreceipt.factoryreceipt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // <-- DODAJ TO
public class FactoryreceiptApplication {
	public static void main(String[] args) {
		SpringApplication.run(FactoryreceiptApplication.class, args);
	}
}
