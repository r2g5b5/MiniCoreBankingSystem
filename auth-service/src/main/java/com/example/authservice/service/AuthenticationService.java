package com.example.authservice.service;

import com.example.authservice.config.JwtService;
import com.example.authservice.dto.AuthenticateRequestDTO;
import com.example.authservice.dto.AuthenticationResponseDTO;
import com.example.authservice.dto.RegisterRequestDTO;
import com.example.authservice.entity.User;
import com.example.authservice.exception.EmailAlreadyExistsException;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("User already exists with email: " + requestDTO.getEmail());
        }
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        User user = userMapper.toEntity(requestDTO);
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticateRequestDTO requestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + requestDTO.getEmail()));

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }
}
