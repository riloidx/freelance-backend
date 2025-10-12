package org.matvey.freelancebackend.security.dto.request;

import jakarta.validation.constraints.Email;
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
public class RegistrationDto {
    @NotBlank(message = "Username is required")
    @Length(min = 3, max = 20, message = "Username must have length between 3 and 20")
    private String username;

    @NotBlank(message = "Name is required")
    @Length(min = 2, max = 50, message = "Name must have length between 2 and 50")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 20, message = "Password must have length between 8 and 20")
    private String password;
}
