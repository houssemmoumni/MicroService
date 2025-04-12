package com.megaminds.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class   GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("PROJECT", r -> r.path("/pro/**")
                        .uri("lb://PROJECT"))
                .route("PROJECT", r -> r.path("/workers/**")
                        .uri("lb://PROJECT"))
                .route("FINANCE", r -> r.path("/api/budgets/**")
                        .uri("lb://FINANCE"))
                .route("FINANCE", r -> r.path("/api/expenses/**")
                        .uri("lb://FINANCE"))
                .route("FINANCCE", r -> r.path("/api/financial-reports/**")
                        .uri("lb://FINANCE"))
                .route("FINANCE", r -> r.path("/api/revenues/**")
                        .uri("lb://FINANCE"))
                .build();
    }

}
