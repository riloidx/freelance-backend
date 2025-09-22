package org.matvey.freelancebackend.users.mapper;

import org.mapstruct.Mapper;
import org.matvey.freelancebackend.security.dto.request.RegisterDto;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntity(RegisterDto dto);

    void updateEntityFromDto(UpdateUserDto dto, User user);
}
