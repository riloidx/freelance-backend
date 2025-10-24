package org.matvey.freelancebackend.ads.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.dto.request.AdCreateDto;
import org.matvey.freelancebackend.ads.dto.request.AdUpdateDto;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.matvey.freelancebackend.ads.service.api.AdCommandService;
import org.matvey.freelancebackend.ads.service.util.AdSecurityUtil;
import org.matvey.freelancebackend.ads.service.util.AdUpdater;
import org.matvey.freelancebackend.ads.service.util.AdValidator;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdCommandServiceImpl implements AdCommandService {
    private final AdRepository adRepo;
    private final AdMapper adMapper;
    private final UserQueryService userQueryService;
    private final CategoryQueryService categoryQueryService;
    private final AdSecurityUtil adSecurityUtil;
    private final AdValidator adValidator;
    private final AdUpdater adUpdater;

    @Override
    public AdResponseDto create(AdCreateDto adCreateDto, Authentication authentication) {
        Ad ad = prepareAd(adCreateDto, authentication);
        Ad saved = adRepo.save(ad);

        return adMapper.toDto(saved);
    }

    @Override
    public AdResponseDto update(AdUpdateDto dto, Authentication authentication) {
        Ad existing = adSecurityUtil.checkAdOwnerPermission(dto.getId(), authentication);

        adUpdater.updateAdFromDto(existing, dto);
        Ad saved = adRepo.save(existing);

        return adMapper.toDto(saved);
    }

    @Override
    public void delete(long id, Authentication authentication) {
        Ad existing = adSecurityUtil.checkAdOwnerPermission(id, authentication);

        adRepo.delete(existing);
    }

    private Ad prepareAd(AdCreateDto adCreateDto, Authentication authentication) {
        User user = userQueryService.findUserByEmail(authentication.getName());
        Category category = categoryQueryService.findCategoryById(adCreateDto.getCategoryId());
        Ad ad = adMapper.toEntity(adCreateDto);
        ad.setUser(user);
        ad.setCategory(category);
        ad.setStatus(AdStatus.ACTIVE);

        return ad;
    }
}

