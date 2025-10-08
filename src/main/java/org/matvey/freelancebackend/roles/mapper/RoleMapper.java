package org.matvey.freelancebackend.roles.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.matvey.freelancebackend.roles.dto.request.RoleCreateDto;
import org.matvey.freelancebackend.roles.dto.response.RoleResponseDto;
import org.matvey.freelancebackend.roles.entity.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    Role toEntity(RoleCreateDto roleCreateDto);

    RoleResponseDto toDto(Role role);

    List<RoleResponseDto> toDto(List<Role> roles);

    default Set<String> mapRolesToStrings(Set<Role> roles) {
        if (roles == null) {
            return new HashSet<>();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
