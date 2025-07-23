package com.example.PersonalProject.service;

import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


@Component
public class JwtService {

    private static final String secretKey = "17145b953423d17ef866c7a55c9941241a3e6a88086d302761681c7eaf7e54e1";

    private final SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String phoneNumber, String userId) {
        return Jwts.builder()
                .subject(phoneNumber)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hour
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

