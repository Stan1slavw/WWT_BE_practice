package com.wwt_be.authapi.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtil {
    private final Key key;
    private final long expMinutes;

    public JWTUtil(@Value("${app.jwt-secret}") String secret,
                   @Value("${app.jwt-exp-min}") long expMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expMinutes = expMinutes;
    }

    public String generateToken(UUID userId, String email) {
        var now = Instant.now();
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expMinutes * 60)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
