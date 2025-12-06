package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for querying user information.
 * 
 * Provides read-only operations for retrieving user data both as entities
 * and DTOs. Supports pagination and various lookup methods by different
 * user identifiers.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
public interface UserQueryService {

    /**
     * Retrieves all users with pagination support.
     * 
     * @param pageable pagination information
     * @return paginated list of user DTOs
     */
    Page<UserResponseDto> findAllUsersDto(Pageable pageable);

    /**
     * Finds a user DTO by their unique identifier.
     * 
     * @param id the user ID
     * @return user response DTO
     * @throws UserNotFoundException if no user exists with the given ID
     */
    UserResponseDto findUserDtoById(long id);

    /**
     * Finds a user DTO by their email address.
     * 
     * @param email the user's email address
     * @return user response DTO
     * @throws UserNotFoundException if no user exists with the given email
     */
    UserResponseDto findUserDtoByEmail(String email);

    /**
     * Finds a user DTO by their username.
     * 
     * @param username the user's username
     * @return user response DTO
     * @throws UserNotFoundException if no user exists with the given username
     */
    UserResponseDto findUserDtoByUsername(String username);

    /**
     * Finds a user entity by their unique identifier.
     * 
     * @param id the user ID
     * @return user entity
     * @throws UserNotFoundException if no user exists with the given ID
     */
    User findUserById(long id);

    /**
     * Finds a user entity by their email address.
     * 
     * @param email the user's email address
     * @return user entity
     * @throws UserNotFoundException if no user exists with the given email
     */
    User findUserByEmail(String email);

    /**
     * Finds a user entity by their username.
     * 
     * @param username the user's username
     * @return user entity
     * @throws UserNotFoundException if no user exists with the given username
     */
    User findUserByUsername(String username);
}