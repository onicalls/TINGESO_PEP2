package com.tingeso.valorlecheservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ValorLecheServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValorLecheServiceApplication.class, args);
	}

}
