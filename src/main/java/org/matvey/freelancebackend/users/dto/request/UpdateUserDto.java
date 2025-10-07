package org.matvey.freelancebackend.users.dto.request;

import org.hibernate.validator.constraints.Length;

public class UpdateUserDto {
    @Length(min = 3, max = 20, message = "Username must have length between 3 and 20")
    private String username;

    @Length(min = 2, max = 50, message = "Name must have length between 2 and 50")
    private String name;

    @Length(max = 3000, message = "Description must have length less then")
    private String description;
}
