package org.matvey.freelancebackend.ads.service.util;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.dto.request.AdUpdateDto;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.mapper.AdMapper;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdUpdater {
    private final AdMapper adMapper;
    private final CategoryQueryService categoryQueryService;

    public void updateAdFromDto(Ad existing, AdUpdateDto dto) {
        if (dto.getCategoryId() != null &&
                (existing.getCategory() == null ||
                        !existing.getCategory().getId().equals(dto.getCategoryId()))) {
            Category category = categoryQueryService.findCategoryById(dto.getCategoryId());
            existing.setCategory(category);
        }

        adMapper.updateEntityFromDto(dto, existing);
    }
}
