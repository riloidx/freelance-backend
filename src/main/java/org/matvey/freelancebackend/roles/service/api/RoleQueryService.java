package org.matvey.freelancebackend.roles.service.api;

import org.matvey.freelancebackend.roles.dto.response.RoleResponseDto;
import org.matvey.freelancebackend.roles.entity.Role;

import java.util.List;

public interface RoleQueryService {
    List<RoleResponseDto> findAllRolesDto();

    Role findRoleById(long id);

    RoleResponseDto findRoleDtoById(long id);

    Role findRoleByName(String name);

    RoleResponseDto findRoleDtoByName(String name);
}
