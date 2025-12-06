package org.matvey.freelancebackend.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.security.dto.request.LoginDto;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.security.dto.response.AuthResponseDto;
import org.matvey.freelancebackend.security.service.api.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user authentication operations.
 * 
 * Provides endpoints for user registration and login functionality.
 * All endpoints are publicly accessible and return JWT tokens upon
 * successful authentication.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Registers a new user in the system.
     * 
     * @param registrationDto the registration data containing user information
     * @return ResponseEntity containing authentication response with JWT tokens
     */
    @PostMapping("/registration")
    public ResponseEntity<AuthResponseDto> registration(@Valid @RequestBody RegistrationDto registrationDto) {
        log.info("Registration request received for email: {}", registrationDto.getEmail());
        AuthResponseDto response = authService.register(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates a user with email and password.
     * 
     * @param loginDto the login credentials containing email and password
     * @return ResponseEntity containing authentication response with JWT tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        log.info("Login request received for email: {}", loginDto.getEmail());
        AuthResponseDto response = authService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
