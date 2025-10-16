package org.matvey.freelancebackend.ads.repository;

import org.matvey.freelancebackend.ads.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findAllByUserId(long userId);
}
