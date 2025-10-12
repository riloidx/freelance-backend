package org.matvey.freelancebackend.category.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDto {
    @NotNull(message = "Id is required field")
    private Long id;
    @Length(min = 2, max = 50, message = "Length of name must be between 2 and 50")
    private String name;
}
