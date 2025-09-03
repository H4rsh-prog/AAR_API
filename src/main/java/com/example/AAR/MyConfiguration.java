package com.example.AAR;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
            	//VULNERABLE TO CORS ATTACKS BUT FINE IN THIS CASE BECAUSE THE DB DOESNT STORE PERSONAL DATA
                registry.addMapping("/**").allowedMethods("*").allowedHeaders("*");
            }
        };
    }
}