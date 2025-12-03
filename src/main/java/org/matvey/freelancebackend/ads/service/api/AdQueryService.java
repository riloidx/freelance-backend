package org.matvey.freelancebackend.ads.service.api;

import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AdQueryService {
    Page<AdResponseDto> findAllByOrderByCreatedDesc(Pageable pageable, Authentication authentication);

    List<AdResponseDto> findAllByUserId(long userId);

    Ad findAdById(long id);

    AdResponseDto findDtoById(long id);

    List<AdResponseDto> findAdsByUser(Authentication authentication, Pageable pageable);
}
