package com.ajcp.service.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class OauthServiceApplication implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(OauthServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String password = "123456";
		for (int i = 0; i < 4; i++) {
			String passwordBCrypt = passwordEncoder.encode(password);
			log.info(passwordBCrypt);
		}
	}
}
