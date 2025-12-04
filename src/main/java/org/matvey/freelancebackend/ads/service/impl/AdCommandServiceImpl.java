package org.matvey.freelancebackend.ads.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.ads.dto.request.AdCreateDto;
import org.matvey.freelancebackend.ads.dto.request.AdUpdateDto;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.matvey.freelancebackend.ads.entity.AdType;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.matvey.freelancebackend.ads.service.api.AdCommandService;
import org.matvey.freelancebackend.ads.service.util.AdSecurityUtil;
import org.matvey.freelancebackend.ads.service.util.AdUpdater;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdCommandServiceImpl implements AdCommandService {
    private final AdRepository adRepo;
    private final AdMapper adMapper;
    private final CategoryQueryService categoryQueryService;
    private final AdSecurityUtil adSecurityUtil;
    private final AdUpdater adUpdater;

    @Override
    @Transactional
    public AdResponseDto create(AdCreateDto adCreateDto, Authentication authentication) {
        log.debug("Creating new ad with title: {}", adCreateDto.getTitleEn());
        try {
            Ad ad = prepareAd(adCreateDto, authentication);
            Ad saved = adRepo.save(ad);
            log.info("Successfully created ad with id: {}", saved.getId());
            return adMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Error creating ad with title: {}", adCreateDto.getTitleEn(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public AdResponseDto update(AdUpdateDto dto, Authentication authentication) {
        log.debug("Updating ad with id: {}", dto.getId());
        try {
            Ad existing = adSecurityUtil.checkAdOwnerPermissionAndReturn(dto.getId(), authentication);
            adUpdater.updateAdFromDto(existing, dto);
            Ad saved = adRepo.save(existing);
            log.info("Successfully updated ad with id: {}", saved.getId());
            return adMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Error updating ad with id: {}", dto.getId(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(long id, Authentication authentication) {
        log.debug("Deleting ad with id: {}", id);
        try {
            //Ad existing = adSecurityUtil.checkAdOwnerPermissionAndReturn(id, authentication);
            adRepo.deleteById(id);
            log.info("Successfully deleted ad with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting ad with id: {}", id, e);
            throw e;
        }
    }

    private Ad prepareAd(AdCreateDto adCreateDto, Authentication authentication) {
        log.debug("Preparing ad for user: {}", authentication.getName());
        User user = ((CustomUserDetails) authentication.getPrincipal()).user();
        Category category = categoryQueryService.findCategoryById(adCreateDto.getCategoryId());
        Ad ad = adMapper.toEntity(adCreateDto);

        ad.setUser(user);
        ad.setCategory(category);
        ad.setStatus(AdStatus.ACTIVE);
        ad.setAdType(AdType.OFFER);
        log.debug("Ad prepared with category id: {} and status: {}", category.getId(), AdStatus.ACTIVE);
        return ad;
    }
}

