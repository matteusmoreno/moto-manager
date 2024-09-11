package com.matteusmoreno.moto_manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8000", "http://localhost:8080", "https://moto-manager-forntend.vercel.app", "https://moto-manager-forntend.vercel.app/", "https://moto-manager-forntend.onrender.com")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
