package com.wwt_be.authapi.service;


import com.wwt_be.authapi.dto.LoginRequest;
import com.wwt_be.authapi.dto.RegisterRequest;
import com.wwt_be.authapi.entity.User;
import com.wwt_be.authapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public User register(RegisterRequest req) {
        if (users.existsByEmail(req.email())) throw new IllegalArgumentException("Email already registered");
        var user = User.builder()
                .email(req.email().toLowerCase())
                .passwordHash(encoder.encode(req.password()))
                .build();
        return users.save(user);
    }

    public User authenticate(LoginRequest req) {
        var user = users.findByEmail(req.email().toLowerCase()).orElseThrow(() -> new IllegalArgumentException("Bad credentials"));
        if (!encoder.matches(req.password(), user.getPasswordHash())) throw new IllegalArgumentException("Bad credentials");
        return user;
    }
}