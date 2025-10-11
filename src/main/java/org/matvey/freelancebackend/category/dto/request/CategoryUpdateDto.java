package org.matvey.freelancebackend.category.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDto {
    @NotNull(message = "Id is required field")
    private Long id;
    private String name;
}
