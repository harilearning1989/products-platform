package com.web.history;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentHistoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentHistoryApplication.class, args);
	}

}
