package org.matvey.freelancebackend.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    UserResponseDto user;
    String accessToken;

}
