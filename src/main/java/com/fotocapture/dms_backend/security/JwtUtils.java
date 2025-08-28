package com.fotocapture.dms_backend.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private final long refreshExpirationMs = 7 * 24 * 60 * 60 * 1000; // 7 days

    // ================================
    // Token generation
    // ================================

    // Generate access token
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        return createToken(claims, username, jwtExpirationMs);
    }

    // Generate refresh token (no roles, just subject)
    public String generateRefreshToken(String username) {
        return createToken(new HashMap<>(), username, refreshExpirationMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // ================================
    // Validation & Claims
    // ================================

    // Validate token signature & expiration
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    // Extract username safely (null if expired/invalid)
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException ex) {
            // Expired but subject still available
            return ex.getClaims().getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

    // Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = getAllClaims(token);
        return claims != null ? resolver.apply(claims) : null;
    }

    // Core claims parser (returns null on error)
    private Claims getAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            // Return claims even if expired → useful for refresh
            return ex.getClaims();
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }

    // ================================
    // Helpers
    // ================================

    public boolean isTokenExpired(String token) {
        Claims claims = getAllClaims(token);
        return claims == null || claims.getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, String username) {
        String extracted = extractUsername(token);
        return extracted != null && extracted.equals(username) && validateToken(token);
    }
}
