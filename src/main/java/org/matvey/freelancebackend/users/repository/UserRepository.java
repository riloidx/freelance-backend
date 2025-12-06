package org.matvey.freelancebackend.users.repository;

import org.matvey.freelancebackend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * 
 * Provides data access methods for user management including
 * queries by username and email, existence checks, and pagination support.
 * Extends JpaRepository for standard CRUD operations.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Finds all users with pagination support.
     * 
     * @param pageable pagination information
     * @return paginated list of users
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Finds a user by their username.
     * 
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     * 
     * @param username the username to check
     * @return true if a user exists with this username
     */
    boolean existsByUsername(String username);

    /**
     * Finds a user by their email address.
     * 
     * @param email the email address to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address.
     * 
     * @param email the email address to check
     * @return true if a user exists with this email
     */
    boolean existsByEmail(String email);
}
