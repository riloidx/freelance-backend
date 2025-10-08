package org.matvey.freelancebackend.roles.service;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.roles.dto.response.RoleResponseDto;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.exception.RoleAlreadyExistsException;
import org.matvey.freelancebackend.roles.exception.RoleNotFoundException;
import org.matvey.freelancebackend.roles.mapper.RoleMapper;
import org.matvey.freelancebackend.roles.repository.RoleRepository;
import org.matvey.freelancebackend.roles.service.api.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepo;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponseDto> findAllRolesDto() {
        List<Role> roles = roleRepo.findAll();

        return roleMapper.toDto(roles);
    }

    @Override
    public Role findRoleById(long id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("id", String.valueOf(id)));
    }

    @Override
    public RoleResponseDto findRoleDtoById(long id) {
        return roleMapper.toDto(findRoleById(id));
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepo.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("name", name));
    }

    @Override
    public RoleResponseDto findRoleDtoByName(String name) {
        return roleMapper.toDto(findRoleByName(name));
    }
}
