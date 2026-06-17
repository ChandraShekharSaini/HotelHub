package com.spring.authservice.service;


import com.spring.authservice.entity.User;
import com.spring.authservice.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        User userObj = userRepository.findByEmail(userEmail);

        log.info("User Details {}", userObj.getEmail());


//        return org.springframework.security.core.userdetails.User
//                .withUsername(userObj.getEmail()).password(userObj.getPassword()).roles("USER").build();

        return new CustomUserDetails(userObj);
    }
}