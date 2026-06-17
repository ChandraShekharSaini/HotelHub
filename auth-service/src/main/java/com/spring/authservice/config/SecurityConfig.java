package com.spring.authservice.config;


import com.spring.authservice.jwtUtils.AuthEntryPointJwt;
import com.spring.authservice.jwtUtils.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AuthEntryPointJwt unAuth;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/login", "/api/v1/auth/register").permitAll().anyRequest().authenticated());
        http.formLogin(form -> form.disable());
        http.logout(logout -> logout.disable());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unAuth));
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
