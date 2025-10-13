package org.matvey.freelancebackend.ads.service.api;

import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdQueryService {
    Page<AdResponseDto> findAllByOrderByCreatedDesc(Pageable pageable);

    AdResponseDto findById(long id);
}
