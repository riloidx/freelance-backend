package org.matvey.freelancebackend.ads.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.exception.AdNotFoundException;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.matvey.freelancebackend.ads.service.api.AdQueryService;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdQueryServiceImpl implements AdQueryService {
    private final AdRepository adRepo;
    private final AdMapper adMapper;
    private final LocalizationUtil localizationUtil;

    @Override
    public Page<AdResponseDto> findAllByOrderByCreatedDesc(Pageable pageable, Authentication authentication) {
        Page<Ad> page;
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.user().getId();
            page = adRepo.findAllByStatusExcludingUserProposals(
                org.matvey.freelancebackend.ads.entity.AdStatus.ACTIVE, userId, pageable);
        } else {
            page = adRepo.findAllByStatus(org.matvey.freelancebackend.ads.entity.AdStatus.ACTIVE, pageable);
        }

        return adMapper.toDto(page);
    }

    @Override
    public List<AdResponseDto> findAllByUserId(long userId) {
        List<Ad> ads = adRepo.findAllByUserId(userId);

        return adMapper.toDto(ads);
    }

    @Override
    public Ad findAdById(long id) {
        return adRepo.findById(id)
                .orElseThrow(() -> new AdNotFoundException(localizationUtil.getMessage("error.ad.not.found", "id", String.valueOf(id))));
    }

    @Override
    public AdResponseDto findDtoById(long id) {
        Ad ad = findAdById(id);

        return adMapper.toDto(ad);
    }

    @Override
    public List<AdResponseDto> findAdsByUser(Authentication authentication, Pageable pageable) {
        List<Ad> ads = adRepo.findAllByUserId(((CustomUserDetails) authentication.getPrincipal()).user().getId());

        return adMapper.toDto(ads);
    }
}
