package org.matvey.freelancebackend.roles.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
import org.matvey.freelancebackend.roles.dto.response.RoleResponseDto;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.exception.RoleNotFoundException;
import org.matvey.freelancebackend.roles.mapper.RoleMapper;
import org.matvey.freelancebackend.roles.repository.RoleRepository;
import org.matvey.freelancebackend.roles.service.api.RoleQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleRepository roleRepo;
    private final RoleMapper roleMapper;
    private final LocalizationUtil localizationUtil;

    @Override
    public List<RoleResponseDto> findAllRolesDto() {
        List<Role> roles = roleRepo.findAll();

        return roleMapper.toDto(roles);
    }

    @Override
    public Role findRoleById(long id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(localizationUtil.getMessage("error.role.not.found", "id", String.valueOf(id))));
    }

    @Override
    public RoleResponseDto findRoleDtoById(long id) {
        return roleMapper.toDto(findRoleById(id));
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepo.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(localizationUtil.getMessage("error.role.not.found", "name", name)));
    }

    @Override
    public RoleResponseDto findRoleDtoByName(String name) {
        return roleMapper.toDto(findRoleByName(name));
    }
}
