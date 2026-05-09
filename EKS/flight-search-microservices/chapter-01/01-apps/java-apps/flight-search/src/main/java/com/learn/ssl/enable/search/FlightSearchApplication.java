package com.learn.ssl.enable.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FlightSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightSearchApplication.class, args);
	}

}
