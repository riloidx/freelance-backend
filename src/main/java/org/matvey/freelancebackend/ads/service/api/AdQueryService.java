package org.matvey.freelancebackend.ads.service.api;

import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdQueryService {
    Page<AdResponseDto> findAllByOrderByCreatedDesc(Pageable pageable);

    List<AdResponseDto> findAllByUserId(long userId);

    AdResponseDto findById(long id);
}
