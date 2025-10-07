package org.matvey.freelancebackend.users.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    @Length(min = 2, max = 50, message = "Name must have length between 2 and 50")
    private String name;

    @Length(max = 3000, message = "Description must have length less then")
    private String description;
}
