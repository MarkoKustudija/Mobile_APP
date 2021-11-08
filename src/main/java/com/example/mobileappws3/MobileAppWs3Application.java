package com.example.mobileappws3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.mobileappws3.security.AppProperties;

@SpringBootApplication
public class MobileAppWs3Application extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder application) {
		return application.sources(MobileAppWs3Application.class);
	}
	

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWs3Application.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}	
	
	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}
	
	@Bean(name = "AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}
	
	
	
	

}