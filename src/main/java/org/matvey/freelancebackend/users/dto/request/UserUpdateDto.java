package org.matvey.freelancebackend.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    @NotNull(message = "id is required field")
    private Long id;

    @Length(min = 2, max = 50, message = "Name must have length between 2 and 50")
    private String name;

    @Length(max = 3000, message = "Description must have length less then")
    private String description;
}
