package org.matvey.freelancebackend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.config.JwtProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for JWT token operations.
 * 
 * Handles JWT token generation, parsing, and validation for both access and refresh tokens.
 * Supports extracting claims, checking token expiration, and validating token authenticity
 * using configurable signing keys and expiration times.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;

    /**
     * Extracts the username from a JWT token.
     * 
     * @param token the JWT token
     * @param isRefresh whether this is a refresh token
     * @return the username from the token subject
     */
    public String extractUsername(String token, boolean isRefresh) {
        return extractClaim(token, Claims::getSubject, isRefresh);
    }

    /**
     * Extracts the expiration date from a JWT token.
     * 
     * @param token the JWT token
     * @param isRefresh whether this is a refresh token
     * @return the expiration date
     */
    public Date extractExpiration(String token, boolean isRefresh) {
        return extractClaim(token, Claims::getExpiration, isRefresh);
    }

    /**
     * Extracts a specific claim from a JWT token.
     * 
     * @param token the JWT token
     * @param claimsResolver function to extract the desired claim
     * @param isRefresh whether this is a refresh token
     * @param <T> the type of the claim
     * @return the extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isRefresh) {
        final Claims claims = extractAllClaims(token, isRefresh);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, boolean isRefresh) {
        Key key = getKey(isRefresh);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getKey(boolean isRefresh) {
        String secret = isRefresh ? jwtProperties.getRefresh().getKey() : jwtProperties.getAccess().getKey();
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates an access token for the given user.
     * 
     * @param userDetails the user details containing username and authorities
     * @return generated access token string
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return generateToken(claims, userDetails, false);
    }

    /**
     * Generates a refresh token for the given user.
     * 
     * @param userDetails the user details containing username
     * @return generated refresh token string
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, true);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, boolean isRefresh) {
        long expiration = isRefresh ? jwtProperties.getRefresh().getExpirationMs()
                : jwtProperties.getAccess().getExpirationMs();
        Key key = getKey(isRefresh);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * Validates a JWT token against user details.
     * 
     * @param token the JWT token to validate
     * @param userDetails the user details to validate against
     * @param isRefresh whether this is a refresh token
     * @return true if the token is valid and not expired
     */
    public boolean isTokenValid(String token, UserDetails userDetails, boolean isRefresh) {
        final String username = extractUsername(token, isRefresh);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, isRefresh));
    }

    private boolean isTokenExpired(String token, boolean isRefresh) {
        return extractExpiration(token, isRefresh).before(new Date());
    }
}
