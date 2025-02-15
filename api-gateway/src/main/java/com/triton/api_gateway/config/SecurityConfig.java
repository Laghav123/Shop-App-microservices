package com.triton.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final String[] freeResourceURLs = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-resources/**",
            "/aggregate/**",
            "/actuator/prometheus",
    };

    // SecurityFilterChain becomes kind of middleware for security
    // it intercepts all the request and check if it requires authorisation
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers(freeResourceURLs).permitAll()
                        .anyRequest().authenticated()     // it means that any request coming needs to be authorised
        ).oauth2ResourceServer(
                oauth2 -> oauth2.jwt(Customizer.withDefaults())     // it means that request will be validated on basis of jwt tokens
        ).cors(Customizer.withDefaults()).build();
    }
}
