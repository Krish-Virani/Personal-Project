package com.example.PersonalProject.service;

import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;


@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String phoneNumber, String userId) {
        return Jwts.builder()
                .subject(phoneNumber)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractPhoneNumber(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserId(String token) {
        Object id = extractAllClaims(token).get("userId");
        return id != null ? id.toString() : null;
    }

    public boolean isTokenValid(String token) {
        try {
            return extractAllClaims(token).getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }
}

