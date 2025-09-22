package org.matvey.freelancebackend.security.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.matvey.freelancebackend.base.dto.request.UserBaseRequestDto;

public class RegisterDto extends UserBaseRequestDto {
    @NotNull
    @Length(min = 8, max = 20, message = "Password must have length between 8 and 20")
    private String password;
}
