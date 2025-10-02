package org.matvey.freelancebackend.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.matvey.freelancebackend.base.dto.response.UserBaseResponseDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;

@SuperBuilder
public class AuthResponseDto extends UserBaseResponseDto {
    String accessToken;
}
