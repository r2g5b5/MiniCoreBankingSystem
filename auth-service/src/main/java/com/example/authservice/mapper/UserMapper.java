package com.example.authservice.mapper;

import com.example.authservice.dto.RegisterRequestDTO;
import com.example.authservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(RegisterRequestDTO registerRequestDTO) {
        return User.builder()
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail())
                .password(registerRequestDTO.getPassword())
                .build();
    }
}
