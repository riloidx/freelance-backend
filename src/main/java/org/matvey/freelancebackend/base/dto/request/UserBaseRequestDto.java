package org.matvey.freelancebackend.base.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseRequestDto {

    @NotNull
    @Length(min = 3, max = 20, message = "Username must have length between 3 and 20")
    private String username;

    @NotNull
    @Length(min = 2, max = 50, message = "Name must have length between 2 and 50")
    private String name;
}

