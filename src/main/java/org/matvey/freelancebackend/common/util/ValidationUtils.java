package org.matvey.freelancebackend.common.util;

import org.matvey.freelancebackend.common.exception.IdMismatchException;

/**
 * Utility class for common validation operations.
 * 
 * Provides static methods for validating data consistency and business rules
 * across the application. Helps ensure data integrity and proper error handling.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
public final class ValidationUtils {
    
    /**
     * Ensures that the ID in the URL path matches the ID in the request body.
     * 
     * This is commonly used in update operations to prevent accidental
     * modification of the wrong entity.
     * 
     * @param entityName the name of the entity being validated (for error messages)
     * @param pathId the ID from the URL path
     * @param dtoId the ID from the request body DTO
     * @throws IdMismatchException if the IDs don't match
     */
    public static void ensureSameId(String entityName, long pathId, long dtoId) {
        if (pathId != dtoId) {
            throw new IdMismatchException(entityName, pathId, dtoId);
        }
    }
}
