package org.matvey.freelancebackend.users.mapper;

import org.mapstruct.Mapper;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntity(RegistrationDto dto);

    void updateEntityFromDto(UpdateUserDto dto, User user);
}
