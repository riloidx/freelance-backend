package org.matvey.freelancebackend.ads.repository;

import org.matvey.freelancebackend.ads.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {
}
