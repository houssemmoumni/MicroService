package com.PIDEV.Blog_Formation_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BlogFormationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogFormationServiceApplication.class, args);
	}

}
