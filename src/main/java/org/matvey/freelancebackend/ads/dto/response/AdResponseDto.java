package org.matvey.freelancebackend.ads.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.matvey.freelancebackend.ads.entity.AdType;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdResponseDto {
    private Long id;
    private String title;
    private String description;
    private AdType adType;
    private BigDecimal budget;
    private AdStatus status;
    private UserResponseDto user;
    private Instant createdAt;
    private CategoryResponseDto category;
}
