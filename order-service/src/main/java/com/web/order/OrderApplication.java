package com.web.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderApplication {

    //http://localhost:8087/swagger-ui.html
    //http://localhost:8087/swagger-ui/index.html
    //http://localhost:8087/v3/api-docs

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
