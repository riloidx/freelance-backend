package org.matvey.freelancebackend.ads.exception;

/**
 * Exception thrown when a requested advertisement cannot be found.
 * 
 * Used throughout the advertisement service layer when ad lookup
 * operations fail. Should be handled by the global exception handler
 * to return appropriate HTTP error responses.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
public class AdNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new AdNotFoundException with the specified detail message.
     * 
     * @param message the detail message explaining why the advertisement was not found
     */
    public AdNotFoundException(String message) {
        super(message);
    }
}
