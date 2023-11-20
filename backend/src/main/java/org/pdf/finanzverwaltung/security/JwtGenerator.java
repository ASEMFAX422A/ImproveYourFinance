package org.pdf.finanzverwaltung.security;

import java.util.Date;

import org.pdf.finanzverwaltung.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtGenerator {

    @Autowired
    private AppConfiguration config;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date expireDate = new Date(new Date().getTime() + config.getJwtExpirationMs());

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(config.getJwtKey())
                .compact();
        return token;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(config.getJwtKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .verifyWith(config.getJwtKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
        }
        return false;
    }
}