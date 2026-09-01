package com.roadnet.service;

import com.roadnet.dto.AuthResponse;
import com.roadnet.dto.LoginRequest;
import com.roadnet.dto.RegisterRequest;
import com.roadnet.dto.UserResponse;
import com.roadnet.exception.ApiException;
import com.roadnet.model.User;
import com.roadnet.repository.UserRepository;
import com.roadnet.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new ApiException("An account with this email already exists", HttpStatus.CONFLICT);
        }

        int age = Period.between(request.getDob(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new ApiException("You must be at least 18 years old to register", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName().trim());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setCountry(request.getCountry());
        user.setCity(request.getCity());
        user.setLanguages(request.getLanguages() == null ? java.util.List.of() : request.getLanguages());
        user.setMaritalStatus(request.getMaritalStatus());
        user.setProfession(request.getProfession());
        user.setBio(request.getBio());
        user.setVerified(false);

        User saved = userRepository.save(user);

        String token = jwtService.generateToken(saved);
        return new AuthResponse(token, userMapper.toResponse(saved));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, userMapper.toResponse(user));
    }
}
