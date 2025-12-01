package org.matvey.freelancebackend.ads.repository;

import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findAllByUserId(long userId);
    Page<Ad> findAllByStatus(AdStatus status, Pageable pageable);
}
