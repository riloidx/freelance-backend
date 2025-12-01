package org.matvey.freelancebackend.ads.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.matvey.freelancebackend.ads.entity.AdType;

import java.math.BigDecimal;

@Data
public class AdUpdateDto {
    @NotNull(message = "Id is required")
    private Long id;

    @NotBlank(message = "Title must not be empty or consist only of spaces")
    @Length(min = 10, max = 50)
    private String title;

    @NotBlank(message = "Description must not be empty or consist only of spaces")
    @Length(min = 10, max = 1500, message = "Length must be between 10 and 1500")
    private String description;

    @NotNull(message = "Ad type is required")
    private AdType adType;

    @Positive(message = "Budget must be positive")
    private BigDecimal budget;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    private AdStatus status;
}
