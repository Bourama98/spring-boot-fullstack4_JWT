package com.amigoscode.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

//    //@Value("#{ '${cors.allowed-origins}'.split(',') }")
//    @Value("#{ '${cors.allowed-origins:}'.isEmpty() ? {'*'} : '${cors.allowed-origins}'.split(',') }")
//    private List<String> allowedOrigins;
//    @Value("#{ '${cors.allowed-methods:}'.isEmpty() ? {'*'} : '${cors.allowed-methods'.split(',')}")
//    private List<String> allowedMethods;
//    @Value("#{ '${cors.allowed-headers:}'.isEmpty() ? {'*'} : '${cors.allowed-headers}'.split(',')}")
//    private List<String> allowedHeaders;
//    @Value("#{ '${cors.exposed-headers:}'.isEmpty() ? {'*'} : '${cors.exposed-headers}'.split(',')}")
//    private List<String> expectedHeaders;

    private List<String> allowedOrigins = List.of("*");
    private List<String> allowedMethods = List.of("*");
    private List<String> allowedHeaders = List.of("*");
    private List<String> expectedHeaders = List.of("*");

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        CorsRegistration corsRegistration = registry.addMapping("/api/**");
//
//        allowedOrigins.forEach(corsRegistration::allowedOrigins);
//        allowedMethods.forEach(corsRegistration::allowedMethods);
//        allowedHeaders.forEach(corsRegistration::allowedHeaders);
//        exposedHeaders.forEach(corsRegistration::exposedHeaders);
//
//
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setExposedHeaders(expectedHeaders);
        configuration.addAllowedHeader(HttpHeaders.AUTHORIZATION);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

}
