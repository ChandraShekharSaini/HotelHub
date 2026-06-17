package com.spring.authservice.controller;

import com.spring.authservice.dtos.UserRequestDto;
import com.spring.authservice.service.AuthService;
import com.spring.authservice.service.AuthServiceImp;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    final AuthServiceImp authServiceImp;

    public AuthController(final AuthServiceImp authServiceImp) {
        this.authServiceImp = authServiceImp;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@RequestBody @Valid UserRequestDto userRequestDto) {
        String registerResponse = authServiceImp.registerUser(userRequestDto);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);

    }
}
