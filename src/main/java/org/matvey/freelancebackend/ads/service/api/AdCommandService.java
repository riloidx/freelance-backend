package org.matvey.freelancebackend.ads.service.api;

import org.matvey.freelancebackend.ads.dto.request.AdCreateDto;
import org.matvey.freelancebackend.ads.dto.request.AdUpdateDto;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.springframework.security.core.Authentication;

public interface AdCommandService {

    AdResponseDto create(AdCreateDto adCreateDto, Authentication authentication);

    AdResponseDto update(AdUpdateDto adUpdateDto, Authentication authentication);

    void delete(long id, Authentication authentication);
}
