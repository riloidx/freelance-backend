package org.matvey.freelancebackend.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class RegistrationDto extends LoginDto {
    @NotNull
    @Length(min = 3, max = 20, message = "Username must have length between 3 and 20")
    private String username;

    @NotNull
    @Length(min = 2, max = 50, message = "Name must have length between 2 and 50")
    private String name;

    @NotNull
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @Length(min = 8, max = 20, message = "Password must have length between 8 and 20")
    private String password;
}
