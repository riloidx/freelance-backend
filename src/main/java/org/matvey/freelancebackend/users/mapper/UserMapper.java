package org.matvey.freelancebackend.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.matvey.freelancebackend.roles.mapper.RoleMapper;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponseDto toDto(User user);

    List<UserResponseDto> toDto(List<User> users);

    @Mapping(target = "roles", ignore = true)
    User toEntity(RegistrationDto dto);

    void updateEntityFromDto(UpdateUserDto dto, @MappingTarget User user);
}
