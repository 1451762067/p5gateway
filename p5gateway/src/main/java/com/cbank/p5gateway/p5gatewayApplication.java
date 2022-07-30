package com.cbank.p5gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class p5gatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(p5gatewayApplication.class, args);
	}
}
