package org.matvey.freelancebackend.ads.repository;

import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for Advertisement entity operations.
 * 
 * Provides data access methods for advertisement management including
 * queries by user, status filtering, and complex queries to exclude
 * ads where users have already submitted proposals.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
public interface AdRepository extends JpaRepository<Ad, Long> {
    
    /**
     * Finds all advertisements created by a specific user.
     * 
     * @param userId the ID of the user who created the ads
     * @return list of advertisements
     */
    List<Ad> findAllByUserId(long userId);
    
    /**
     * Finds all advertisements with a specific status.
     * 
     * @param status the advertisement status to filter by
     * @param pageable pagination information
     * @return paginated list of advertisements with the given status
     */
    Page<Ad> findAllByStatus(AdStatus status, Pageable pageable);
    
    /**
     * Finds advertisements with a specific status excluding those where
     * the given user has already submitted proposals.
     * 
     * This is useful for showing available jobs to freelancers without
     * displaying ads they've already applied to.
     * 
     * @param status the advertisement status to filter by
     * @param userId the user ID to exclude proposals for
     * @param pageable pagination information
     * @return paginated list of available advertisements
     */
    @Query("SELECT a FROM Ad a WHERE a.status = :status AND a.id NOT IN " +
           "(SELECT p.ad.id FROM Proposal p WHERE p.freelancer.id = :userId)")
    Page<Ad> findAllByStatusExcludingUserProposals(@Param("status") AdStatus status, 
                                                     @Param("userId") Long userId, 
                                                     Pageable pageable);
}
