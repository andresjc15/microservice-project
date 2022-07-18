package com.ajcp.service.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EntityScan({"com.ajcp.service.common.user"})
@EnableEurekaClient
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "User service",
				version = "1.0.0",
				description = "User Microservice"))
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
