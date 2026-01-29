package com.example.utility;

import com.example.entity.User;
import com.example.exceptions.GenericException;
import com.example.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class generates JWT tokens for authenticated users.
 * Tokens include user roles and profile info as claims.
 */
@Component
public class JwtUtils {

    private final String secret = "TE86asbjkw32390djsksbfkh93nklsnlsaifw98ewyshnjxbscjkb";

    private final UserRepository userRepository;

    public JwtUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Generates JWT token for a given username
     */
    public String generateToken(String username) {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new GenericException(
                        HttpStatus.NOT_FOUND.value(),
                        "User not found with username: " + username
                ));
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", sanitizeRoles(user.getRoles()));
        claims.put("firstName", sanitizeString(user.getFirstName()));
        claims.put("lastName", sanitizeString(user.getLastName()));

        return createToken(claims, user.getUsername());
    }

    /**
     * Creates the JWT token with claims, subject, issuedAt, expiration
     */
    private String createToken(Map<String, Object> claims, String subject) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600_000);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key())
                .compact();
    }

    /**
     * Returns the SecretKey object for signing the token
     */
    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Sanitize string values to avoid malicious content in JWT claims
     */
    private String sanitizeString(String input) {
        return input == null ? "" : input.replaceAll("[^\\p{L}\\p{Nd} ]", "");
    }

    /**
     * Sanitize roles to avoid injection
     */
    private Object sanitizeRoles(Object roles) {
        return roles;
    }

    public String extractUsername(String token) {
        return extractALlClaims(token).getSubject();
    }

    /**
     *
     * @param token
     * @return claims to get more details or validate token
     */
    private Claims extractALlClaims(String token){
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, String username) {
        var tokenUsername = extractALlClaims(token).getSubject();
        return username.equals(tokenUsername);
    }

    public boolean isTokenExpire(String token) {
        return extractExpire(token).before(new Date());
    }

    private Date extractExpire(String token) {
        return extractALlClaims(token).getExpiration();
    }
}
