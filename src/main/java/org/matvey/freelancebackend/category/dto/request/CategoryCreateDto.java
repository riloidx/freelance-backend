package org.matvey.freelancebackend.category.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateDto {
    @Length(min = 2, max = 32, message = "Length of nameEn must be between 2 and 32")
    private String nameEn;
    
    @Length(min = 2, max = 32, message = "Length of nameRu must be between 2 and 32")
    private String nameRu;
}
