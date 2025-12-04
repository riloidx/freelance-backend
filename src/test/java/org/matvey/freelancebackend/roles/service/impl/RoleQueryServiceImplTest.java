package org.matvey.freelancebackend.roles.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
import org.matvey.freelancebackend.roles.dto.response.RoleResponseDto;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.exception.RoleNotFoundException;
import org.matvey.freelancebackend.roles.mapper.RoleMapper;
import org.matvey.freelancebackend.roles.repository.RoleRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleQueryServiceImplTest {

    @Mock
    private RoleRepository roleRepo;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private LocalizationUtil localizationUtil;

    @InjectMocks
    private RoleQueryServiceImpl roleQueryService;

    private Role role;
    private RoleResponseDto roleResponseDto;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("USER");

        roleResponseDto = new RoleResponseDto();
        roleResponseDto.setId(1L);
        roleResponseDto.setName("USER");
    }

    @Test
    void FindAllRolesDtoShouldReturnListOfRoles() {
        List<Role> roles = List.of(role);
        List<RoleResponseDto> expectedDtos = List.of(roleResponseDto);

        when(roleRepo.findAll()).thenReturn(roles);
        when(roleMapper.toDto(roles)).thenReturn(expectedDtos);

        List<RoleResponseDto> result = roleQueryService.findAllRolesDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roleRepo).findAll();
        verify(roleMapper).toDto(roles);
    }

    @Test
    void FindRoleByIdShouldReturnRole() {
        when(roleRepo.findById(1L)).thenReturn(Optional.of(role));

        Role result = roleQueryService.findRoleById(1L);

        assertNotNull(result);
        assertEquals(role.getId(), result.getId());
        verify(roleRepo).findById(1L);
    }

    @Test
    void FindRoleByIdShouldThrowExceptionWhenNotFound() {
        when(roleRepo.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RoleNotFoundException.class,
                () -> roleQueryService.findRoleById(1L));

        assertNotNull(exception);
        verify(roleRepo).findById(1L);
    }

    @Test
    void FindRoleDtoByIdShouldReturnRoleResponseDto() {
        when(roleRepo.findById(1L)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleResponseDto);

        RoleResponseDto result = roleQueryService.findRoleDtoById(1L);

        assertNotNull(result);
        assertEquals(roleResponseDto.getId(), result.getId());
        verify(roleRepo).findById(1L);
        verify(roleMapper).toDto(role);
    }

    @Test
    void FindRoleByNameShouldReturnRole() {
        when(roleRepo.findByName("USER")).thenReturn(Optional.of(role));

        Role result = roleQueryService.findRoleByName("USER");

        assertNotNull(result);
        assertEquals(role.getName(), result.getName());
        verify(roleRepo).findByName("USER");
    }

    @Test
    void FindRoleByNameShouldThrowExceptionWhenNotFound() {
        when(roleRepo.findByName("USER")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RoleNotFoundException.class,
                () -> roleQueryService.findRoleByName("USER"));

        assertNotNull(exception);
        verify(roleRepo).findByName("USER");
    }

    @Test
    void FindRoleDtoByNameShouldReturnRoleResponseDto() {
        when(roleRepo.findByName("USER")).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleResponseDto);

        RoleResponseDto result = roleQueryService.findRoleDtoByName("USER");

        assertNotNull(result);
        assertEquals(roleResponseDto.getName(), result.getName());
        verify(roleRepo).findByName("USER");
        verify(roleMapper).toDto(role);
    }
}