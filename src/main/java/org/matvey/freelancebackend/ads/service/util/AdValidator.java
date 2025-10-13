package org.matvey.freelancebackend.ads.service.util;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.exception.AdNotFoundException;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdValidator {
    private final AdRepository adRepo;

    public Ad findExistingAd(long id) {
        return adRepo.findById(id)
                .orElseThrow(() -> new AdNotFoundException("id", String.valueOf(id)));
    }
}
