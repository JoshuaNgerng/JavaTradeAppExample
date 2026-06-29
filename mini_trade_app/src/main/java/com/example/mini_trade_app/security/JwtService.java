package com.example.mini_trade_app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import com.example.mini_trade_app.config.JwtProperties;
import com.example.mini_trade_app.user.entity.RoleType;

public class JwtService {

    private final Key   key;
    private final long  expiration;

    public JwtService(JwtProperties config) {
        this.key = Keys.hmacShaKeyFor(config.secret().getBytes());
        this.expiration = config.expiration().toSeconds();
    }

    public String generateToken(Long userId, RoleType userRoles) {

        return Jwts
            .builder()
            .setSubject(String.valueOf(userId))
            .claim("role", userRoles.toString())
            .setIssuedAt(new Date())
            .setExpiration(
                new Date(
                    System.currentTimeMillis() + expiration
                )
            )
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractUserId(String token) {
        return getClaims(token).getSubject();
    }

    public String extractUserRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean validate(String token) {
        try {
            // Extract expiration
            Date expirationDate = getClaims(token).getExpiration();

            if (expirationDate.before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}