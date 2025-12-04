package org.matvey.freelancebackend.roles.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
import org.matvey.freelancebackend.roles.dto.response.RoleResponseDto;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.exception.RoleNotFoundException;
import org.matvey.freelancebackend.roles.mapper.RoleMapper;
import org.matvey.freelancebackend.roles.repository.RoleRepository;
import org.matvey.freelancebackend.roles.service.api.RoleQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleRepository roleRepo;
    private final RoleMapper roleMapper;
    private final LocalizationUtil localizationUtil;

    @Override
    public List<RoleResponseDto> findAllRolesDto() {
        log.debug("Finding all roles");
        try {
            List<Role> roles = roleRepo.findAll();
            log.info("Found {} roles", roles.size());
            return roleMapper.toDto(roles);
        } catch (Exception e) {
            log.error("Error finding all roles", e);
            throw e;
        }
    }

    @Override
    public Role findRoleById(long id) {
        log.debug("Finding role by id: {}", id);
        try {
            Role role = roleRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Role not found with id: {}", id);
                        return new RoleNotFoundException(localizationUtil.getMessage("error.role.not.found", "id", String.valueOf(id)));
                    });
            log.debug("Found role with id: {}", id);
            return role;
        } catch (RoleNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding role by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public RoleResponseDto findRoleDtoById(long id) {
        return roleMapper.toDto(findRoleById(id));
    }

    @Override
    public Role findRoleByName(String name) {
        log.debug("Finding role by name: {}", name);
        try {
            Role role = roleRepo.findByName(name)
                    .orElseThrow(() -> {
                        log.warn("Role not found with name: {}", name);
                        return new RoleNotFoundException(localizationUtil.getMessage("error.role.not.found", "name", name));
                    });
            log.debug("Found role with name: {}", name);
            return role;
        } catch (RoleNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding role by name: {}", name, e);
            throw e;
        }
    }

    @Override
    public RoleResponseDto findRoleDtoByName(String name) {
        return roleMapper.toDto(findRoleByName(name));
    }
}
