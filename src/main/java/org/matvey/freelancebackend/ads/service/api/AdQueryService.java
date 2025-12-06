package org.matvey.freelancebackend.ads.service.api;

import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Service interface for querying job advertisements.
 * 
 * Provides read-only operations for retrieving advertisement data with various
 * filtering and sorting options. Supports pagination and user-specific queries
 * with proper authentication context.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
public interface AdQueryService {
    
    /**
     * Retrieves all advertisements ordered by creation date (newest first).
     * 
     * @param pageable pagination information
     * @param authentication current user authentication context
     * @return paginated list of advertisement DTOs
     */
    Page<AdResponseDto> findAllByOrderByCreatedDesc(Pageable pageable, Authentication authentication);

    /**
     * Finds all advertisements created by a specific user.
     * 
     * @param userId the ID of the user who created the ads
     * @return list of advertisement DTOs
     */
    List<AdResponseDto> findAllByUserId(long userId);

    /**
     * Finds an advertisement entity by its unique identifier.
     * 
     * @param id the advertisement ID
     * @return advertisement entity
     * @throws AdNotFoundException if no advertisement exists with the given ID
     */
    Ad findAdById(long id);

    /**
     * Finds an advertisement DTO by its unique identifier.
     * 
     * @param id the advertisement ID
     * @return advertisement response DTO
     * @throws AdNotFoundException if no advertisement exists with the given ID
     */
    AdResponseDto findDtoById(long id);

    /**
     * Finds advertisements created by the authenticated user.
     * 
     * @param authentication current user authentication context
     * @param pageable pagination information
     * @return list of advertisement DTOs belonging to the authenticated user
     */
    List<AdResponseDto> findAdsByUser(Authentication authentication, Pageable pageable);
}
