package com.fotocapture.dms_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000","http://localhost:3001","http://localhost:5173",   // <-- add this
                                "http://127.0.0.1:5173")
                        .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                        .allowCredentials(false);
            }
        };
    }
}
