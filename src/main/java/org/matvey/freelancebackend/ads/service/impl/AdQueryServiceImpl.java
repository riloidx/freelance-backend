package org.matvey.freelancebackend.ads.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.exception.AdNotFoundException;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.matvey.freelancebackend.ads.service.api.AdQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdQueryServiceImpl implements AdQueryService {
    private final AdRepository adRepo;
    private final AdMapper adMapper;

    @Override
    public Page<AdResponseDto> findAllByOrderByCreatedDesc(Pageable pageable) {
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("createdAt"));

        Page<Ad> page = adRepo.findAll(sorted);

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
                .orElseThrow(() -> new AdNotFoundException("id", String.valueOf(id)));
    }

    @Override
    public AdResponseDto findDtoById(long id) {
        Ad ad = findAdById(id);

        return adMapper.toDto(ad);
    }
}
