package org.matvey.freelancebackend.ads.service.util;

import org.matvey.freelancebackend.ads.entity.Ad;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AdSecurityUtil {

    public void checkUpdatePermission(Ad ad, Authentication auth) {
        if (isOwner(ad, auth) || auth == null) {
            throw new AccessDeniedException("You are not allowed to update this ad");
        }
    }

    public void checkDeletePermission(Ad ad, Authentication auth) {
        if (isOwner(ad, auth) || auth == null) {
            throw new AccessDeniedException("You are not allowed to delete this ad");
        }
    }

    private boolean isOwner(Ad ad, Authentication auth) {
        if (ad.getUser() == null) return true;
        return !auth.getName().equals(ad.getUser().getEmail());
    }
}
