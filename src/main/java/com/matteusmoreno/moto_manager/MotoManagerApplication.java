package com.matteusmoreno.moto_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MotoManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotoManagerApplication.class, args);
	}

}
