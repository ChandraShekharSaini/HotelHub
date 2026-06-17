package com.spring.authservice.jwtUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        logger.warn("Unauthorized access to {} - {}",
                request.getRequestURI(),
                authException.getMessage());

        String token = jwtUtils.getJwtTokenFromHeader(request);


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> errors = new HashMap<>();

        errors.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errors.put("error", "Unauthorized");
        errors.put("message", authException.getMessage());
        errors.put("requestURI", request.getServletPath());
        errors.put("timestamp", LocalDateTime.now().toString());
        errors.put("token", token);

        // Convert into Json
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errors);

    }
}