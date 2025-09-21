package org.matvey.freelancebackend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.config.JwtProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String extractUsername(String token, boolean isRefresh) {
        return extractClaim(token, Claims::getSubject, isRefresh);
    }

    public Date extractExpiration(String token, boolean isRefresh) {
        return extractClaim(token, Claims::getExpiration, isRefresh);
    }

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

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, false);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, true);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, boolean isRefresh) {
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

    public boolean isTokenValid(String token, UserDetails userDetails, boolean isRefresh) {
        final String username = extractUsername(token, isRefresh);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, isRefresh));
    }

    private boolean isTokenExpired(String token, boolean isRefresh) {
        return extractExpiration(token, isRefresh).before(new Date());
    }
}
