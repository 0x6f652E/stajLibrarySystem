// src/main/java/com/ibb/library/security/JwtUtil.java
package com.ibb.library.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMs;

    // application.properties:
    // application.jwt.secret=... (32+ char)
    // application.jwt.expiration=86400000
    public JwtUtil(@Value("${application.jwt.secret}") String secret,
                   @Value("${application.jwt.expiration}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token) {
        Object role = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("role");
        return role == null ? "USER" : role.toString();
    }

    /** AuthServiceImpl’in çağırdığı imza: subject + ekstra claims */
    public String generateToken(String subject, Map<String, String> extraClaims) {
        long now = System.currentTimeMillis();

        // String değerli map'i Object değerli map'e taşıyoruz
        Map<String, Object> claims = new HashMap<>();
        if (extraClaims != null) claims.putAll(extraClaims);

        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Pratik overload: sadece subject + role ver, token üret */
    public String generateToken(String subject, String role) {
        Map<String, String> claims = new HashMap<>();
        if (role != null) claims.put("role", role);
        return generateToken(subject, claims);
    }
}
