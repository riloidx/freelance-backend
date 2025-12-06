package org.matvey.freelancebackend.users.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested user cannot be found.
 * 
 * This exception is automatically mapped to HTTP 404 Not Found status
 * when thrown from REST controllers. Used throughout the user service
 * layer when user lookup operations fail.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     * 
     * @param message the detail message explaining why the user was not found
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}