package org.matvey.freelancebackend.security.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginDto {
    @NotNull
    @Length(min = 3, max = 20, message = "Username must have length between 8 and 20")
    private String username;
    @NotNull
    @Length(min = 8, max = 20, message = "Password must have length between 8 and 20")
    private String password;
}
