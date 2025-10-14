package com.linktic.pruebacompralinktic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.linktic.pruebacompralinktic.client")
public class PruebacompralinkticApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebacompralinkticApplication.class, args);
	}

}
