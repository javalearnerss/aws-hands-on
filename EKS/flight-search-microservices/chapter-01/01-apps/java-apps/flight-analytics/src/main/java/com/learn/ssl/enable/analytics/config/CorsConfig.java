package com.learn.ssl.enable.analytics.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * CORS configuration for Flight Search application.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private CorsProperties cors;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(convertToArray(cors.getAllowedOrigins()))
                .allowedMethods(convertToArray(cors.getAllowedMethods()))
                .allowedHeaders(convertToArray(cors.getAllowedHeaders()))
                .exposedHeaders(convertToArray(cors.getExposedHeaders()))
                .allowCredentials(cors.isAllowCredentials())
                .maxAge(cors.getMaxAge());
    }

    public String[] convertToArray(String input) {

        return Arrays.stream(input.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}