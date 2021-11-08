package com.example.mobileappws3;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	public void addCorsMapping(CorsRegistry registry) {
		
	// ovaj se gornji primer odnosi samo na 1 origin i na 1 RestController
		
		/*registry.addMapping("/api/filmovi")
		.allowedOrigins("http://localhost:3000")
		.allowedMethods("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
		.allowedHeaders("*")
		.exposedHeaders("Total-Pages")
		.allowCredentials(true)
		.maxAge(3600);*/
		
		
		
		registry
		.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods( "HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS");
//		.allowedHeaders("Authorization","content-type")
//		.exposedHeaders("Total-Pages, Sprint-Total")
//		.allowCredentials(true)
//		.maxAge(3600);
			
	}
}
