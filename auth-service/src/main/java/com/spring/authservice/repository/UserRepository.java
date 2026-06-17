package com.spring.authservice.repository;

import com.spring.authservice.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Please provide a valid email address") String email);
}
