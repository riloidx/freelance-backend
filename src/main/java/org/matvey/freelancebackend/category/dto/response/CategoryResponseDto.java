package org.matvey.freelancebackend.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private long id;
    private String nameEn;
    private String nameRu;
    private int adsCount;
}
