package org.matvey.freelancebackend.ads.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.exception.AdNotFoundException;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.matvey.freelancebackend.ads.service.api.AdQueryService;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdQueryServiceImpl implements AdQueryService {
    private final AdRepository adRepo;
    private final AdMapper adMapper;
    private final LocalizationUtil localizationUtil;

    @Override
    public Page<AdResponseDto> findAllByOrderByCreatedDesc(Pageable pageable, Authentication authentication) {
        log.debug("Finding all ads with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Ad> page;
            if (authentication != null && authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                Long userId = userDetails.user().getId();
                log.debug("Finding ads excluding user proposals for userId: {}", userId);
                page = adRepo.findAllByStatusExcludingUserProposals(
                        org.matvey.freelancebackend.ads.entity.AdStatus.ACTIVE, userId, pageable);
            } else {
                log.debug("Finding all active ads for unauthenticated user");
                page = adRepo.findAllByStatus(org.matvey.freelancebackend.ads.entity.AdStatus.ACTIVE, pageable);
            }
            log.info("Found {} ads", page.getTotalElements());
            return adMapper.toDto(page);
        } catch (Exception e) {
            log.error("Error finding ads", e);
            throw e;
        }
    }

    @Override
    public List<AdResponseDto> findAllByUserId(long userId) {
        log.debug("Finding all ads for userId: {}", userId);
        try {
            List<Ad> ads = adRepo.findAllByUserId(userId);
            log.info("Found {} ads for userId: {}", ads.size(), userId);
            return adMapper.toDto(ads);
        } catch (Exception e) {
            log.error("Error finding ads for userId: {}", userId, e);
            throw e;
        }
    }

    @Override
    public Ad findAdById(long id) {
        log.debug("Finding ad by id: {}", id);
        try {
            Ad ad = adRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Ad not found with id: {}", id);
                        return new AdNotFoundException(localizationUtil.getMessage("error.ad.not.found", "id", String.valueOf(id)));
                    });
            log.debug("Found ad with id: {}", id);
            return ad;
        } catch (AdNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding ad by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public AdResponseDto findDtoById(long id) {
        log.debug("Finding ad DTO by id: {}", id);
        Ad ad = findAdById(id);
        return adMapper.toDto(ad);
    }

    @Override
    public List<AdResponseDto> findAdsByUser(Authentication authentication, Pageable pageable) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).user().getId();
        log.debug("Finding ads by user with userId: {}", userId);
        try {
            List<Ad> ads = adRepo.findAllByUserId(userId);
            log.info("Found {} ads for current user", ads.size());
            return adMapper.toDto(ads);
        } catch (Exception e) {
            log.error("Error finding ads for current user", e);
            throw e;
        }
    }
}
