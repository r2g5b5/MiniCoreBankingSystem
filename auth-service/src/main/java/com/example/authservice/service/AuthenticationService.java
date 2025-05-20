package com.example.authservice.service;

import com.example.authservice.config.JwtService;
import com.example.authservice.dto.AuthenticateRequestDTO;
import com.example.authservice.dto.AuthenticationResponseDTO;
import com.example.authservice.dto.RegisterRequestDTO;
import com.example.authservice.entity.User;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthenticationResponseDTO register(RegisterRequestDTO requestDTO) {
        User user = userMapper.toEntity(requestDTO);
        String token = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder().token(token).build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticateRequestDTO requestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));

        User user = userRepository.findByEmail(requestDTO.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
