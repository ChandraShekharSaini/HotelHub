package com.spring.authservice.service;

import com.spring.authservice.dtos.UserRequestDto;
import com.spring.authservice.entity.User;
import com.spring.authservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {

    final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthServiceImp(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public String registerUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RuntimeException("User already exists with email: "
                    + userRequestDto.getEmail());
        }

        User user = modelMapper.map(userRequestDto, User.class);

        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userRepository.save(user);

        return "User registered successfully";
    }

}
