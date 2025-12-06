package org.matvey.freelancebackend.security.service.api;

import org.matvey.freelancebackend.security.dto.request.LoginDto;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.security.dto.response.AuthResponseDto;
import org.matvey.freelancebackend.users.entity.User;

/**
 * Service interface for handling user authentication operations.
 * 
 * Provides methods for user login and registration, including JWT token generation
 * and user validation. Handles the core authentication flow for the freelance platform.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
public interface AuthService {
    
    /**
     * Authenticates a user with email and password.
     * 
     * @param loginDto the login credentials containing email and password
     * @return AuthResponseDto containing JWT tokens and user information
     * @throws InvalidCredentialsException if the credentials are invalid
     */
    AuthResponseDto login(LoginDto loginDto);

    /**
     * Registers a new user in the system.
     * 
     * @param registrationDto the registration data containing user information
     * @return AuthResponseDto containing JWT tokens and user information
     * @throws UserAlreadyExistsException if a user with the same email already exists
     */
    AuthResponseDto register(RegistrationDto registrationDto);
}
