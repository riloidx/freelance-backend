package org.matvey.freelancebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Main application configuration class.
 * 
 * Provides common beans and configuration for the freelance platform
 * including password encoding and Spring Data Web support for pagination.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Component
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ApplicationConfig {
    
    /**
     * Provides a BCrypt password encoder bean for secure password hashing.
     * 
     * @return BCryptPasswordEncoder instance for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
