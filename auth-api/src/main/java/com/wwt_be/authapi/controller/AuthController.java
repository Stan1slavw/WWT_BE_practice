package com.wwt_be.authapi.controller;


import com.wwt_be.authapi.config.JWTUtil;
import com.wwt_be.authapi.dto.AuthResponse;
import com.wwt_be.authapi.dto.LoginRequest;
import com.wwt_be.authapi.dto.RegisterRequest;
import com.wwt_be.authapi.entity.User;
import com.wwt_be.authapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JWTUtil jwt;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest req) {
        User user = authService.authenticate(req);
        return new AuthResponse(jwt.generateToken(user.getId(), user.getEmail()));
    }
}