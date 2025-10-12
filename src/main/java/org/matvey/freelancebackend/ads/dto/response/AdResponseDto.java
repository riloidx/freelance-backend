package org.matvey.freelancebackend.ads.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matvey.freelancebackend.ads.entity.AdType;
import org.matvey.freelancebackend.category.entity.Category;
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
    private AdType AdType;
    private BigDecimal budget;
    private String status;
    private User user;
    private Instant createdAt;
    private Category category;
}
