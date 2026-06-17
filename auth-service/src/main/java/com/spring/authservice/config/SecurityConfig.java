package com.spring.authservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/api/v1/register", "/api/v1/login").permitAll().anyRequest().authenticated())
                .formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout.disable());

        return http.build();

    }
}
