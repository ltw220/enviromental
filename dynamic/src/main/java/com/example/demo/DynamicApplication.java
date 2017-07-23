package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DynamicApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicApplication.class, args);
	}
	
	@Bean
	public String something(Environment environment, SomeProperties someProperties) {
	
		boolean outcome = environment.containsProperty("filters[0].name");
		
		
		return "Something";
	}
	
	@Bean
	public String somethingElse(Environment environment, SomeProperties someProperties) {
	
		boolean outcome = environment.containsProperty("filters[0].name");
		
		
		return "SomethingElse";
	}
	
	
}
