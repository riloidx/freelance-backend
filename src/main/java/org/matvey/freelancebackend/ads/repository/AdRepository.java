package org.matvey.freelancebackend.ads.repository;

import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findAllByUserId(long userId);
    Page<Ad> findAllByStatus(AdStatus status, Pageable pageable);
    
    @Query("SELECT a FROM Ad a WHERE a.status = :status AND a.id NOT IN " +
           "(SELECT p.ad.id FROM Proposal p WHERE p.freelancer.id = :userId)")
    Page<Ad> findAllByStatusExcludingUserProposals(@Param("status") AdStatus status, 
                                                     @Param("userId") Long userId, 
                                                     Pageable pageable);
}
