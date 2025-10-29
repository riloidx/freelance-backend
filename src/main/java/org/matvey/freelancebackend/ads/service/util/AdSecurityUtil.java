package org.matvey.freelancebackend.ads.service.util;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.service.api.AdQueryService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdSecurityUtil {
    private final AdQueryService adQueryService;

    public Ad checkAdOwnerPermission(long adId, Authentication auth) {
        Ad ad = adQueryService.findAdById(adId);

        if (auth == null || !auth.getName().equals(ad.getUser().getEmail())) {
            throw new AccessDeniedException("You are not allowed to this ad");
        }

        return ad;
    }
}
