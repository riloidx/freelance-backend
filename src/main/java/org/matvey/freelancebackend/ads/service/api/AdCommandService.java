package org.matvey.freelancebackend.ads.service.api;

import org.matvey.freelancebackend.ads.dto.request.AdCreateDto;
import org.matvey.freelancebackend.ads.dto.request.AdUpdateDto;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.springframework.security.core.Authentication;

/**
 * Service interface for managing job advertisement operations.
 * 
 * Handles create, update, and delete operations for job advertisements.
 * All operations require proper authentication and authorization to ensure
 * users can only modify their own advertisements.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
public interface AdCommandService {

    /**
     * Creates a new job advertisement.
     * 
     * @param adCreateDto the advertisement creation data
     * @param authentication current user authentication context
     * @return created advertisement response DTO
     * @throws CategoryNotFoundException if the specified category doesn't exist
     */
    AdResponseDto create(AdCreateDto adCreateDto, Authentication authentication);

    /**
     * Updates an existing job advertisement.
     * 
     * @param adUpdateDto the advertisement update data
     * @param authentication current user authentication context
     * @return updated advertisement response DTO
     * @throws AdNotFoundException if the advertisement doesn't exist
     * @throws UnauthorizedException if the user doesn't own the advertisement
     */
    AdResponseDto update(AdUpdateDto adUpdateDto, Authentication authentication);

    /**
     * Deletes a job advertisement.
     * 
     * @param id the advertisement ID to delete
     * @param authentication current user authentication context
     * @throws AdNotFoundException if the advertisement doesn't exist
     * @throws UnauthorizedException if the user doesn't own the advertisement
     */
    void delete(long id, Authentication authentication);
}
