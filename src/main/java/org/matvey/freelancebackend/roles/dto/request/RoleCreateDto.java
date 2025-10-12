package org.matvey.freelancebackend.roles.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateDto {
    @NotBlank(message = "Name must not be empty or consist only of spaces")
    @Length(min = 2, max = 50, message = "Length of name must be between 2 and 50")
    private String name;
}
