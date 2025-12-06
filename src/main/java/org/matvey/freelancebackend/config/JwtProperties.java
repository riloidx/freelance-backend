package org.matvey.freelancebackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for JWT token settings.
 * 
 * Binds JWT-related configuration from application properties file
 * including separate settings for access and refresh tokens with
 * their respective signing keys and expiration times.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private Token access;
    private Token refresh;

    /**
     * Inner class representing token configuration.
     * 
     * Contains the signing key and expiration time for JWT tokens.
     */
    @Data
    public static class Token {
        private String key;
        private long expirationMs;

    }
}