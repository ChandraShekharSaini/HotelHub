package com.spring.authservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    private String password;

    private String email;
}