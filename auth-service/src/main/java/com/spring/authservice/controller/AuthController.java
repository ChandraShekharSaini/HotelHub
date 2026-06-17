package com.spring.authservice.controller;

import com.spring.authservice.dtos.LoginRequest;
import com.spring.authservice.dtos.LoginResponse;
import com.spring.authservice.dtos.UserRequestDto;
import com.spring.authservice.jwtUtils.JwtUtils;
import com.spring.authservice.service.AuthServiceImp;
import com.spring.authservice.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static Logger log = LoggerFactory.getLogger(AuthController.class);

//    @Autowired
//    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    final AuthServiceImp authServiceImp;

    public AuthController(final AuthServiceImp authServiceImp) {
        this.authServiceImp = authServiceImp;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest userReq, HttpServletRequest request) {

        log.info("===========Login request received=========={}", userReq.getEmail());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userReq.getEmail(), userReq.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> errors = new HashMap<>();

            errors.put("status", HttpStatus.NOT_FOUND);
            errors.put("message", e.getMessage());
            errors.put("requestUri", request.getRequestURI());
            errors.put("timestamp", LocalDateTime.now());

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        log.debug("UserName: {}", userDetails.getUsername());
        log.debug("Password: {}", userDetails.getPassword());


        String token = jwtUtils.generateJwtToken(userDetails);

        LoginResponse response = new LoginResponse();
        response.setId(userDetails.getId());
        response.setEmail(userDetails.getEmail());
        response.setUsername(userDetails.getUsername());
        response.setCreatedAt(LocalDateTime.now());
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@RequestBody @Valid UserRequestDto userRequestDto) {
        String registerResponse = authServiceImp.registerUser(userRequestDto);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);

    }



}
