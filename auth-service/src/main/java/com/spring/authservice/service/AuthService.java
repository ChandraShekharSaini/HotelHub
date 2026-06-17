package com.spring.authservice.service;

import com.spring.authservice.dtos.UserRequestDto;

public interface AuthService {

    String registerUser(UserRequestDto userRequestDto);
}
