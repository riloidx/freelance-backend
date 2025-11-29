package org.matvey.freelancebackend.users.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.service.api.RoleQueryService;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserAlreadyHasRoleException;
import org.matvey.freelancebackend.users.exception.UserDoesntHasRoleException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserQueryService userQueryService;
    
    @Mock
    private RoleQueryService roleQueryService;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private AdminServiceImpl adminService;
    
    private User user;
    private Role role;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        
        user = new User();
        user.setId(1L);
        user.setRoles(new HashSet<>());
        
        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
    }

    @Test
    void AddRoleToUserShouldAddRoleSuccessfully() {
        when(userQueryService.findUserById(1L)).thenReturn(user);
        when(roleQueryService.findRoleByName("ADMIN")).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.addRoleToUser("ADMIN", 1L);

        assertNotNull(result);
        assertTrue(user.getRoles().contains(role));
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void AddRoleToUserShouldThrowExceptionWhenUserAlreadyHasRole() {
        user.getRoles().add(role);
        when(userQueryService.findUserById(1L)).thenReturn(user);
        when(roleQueryService.findRoleByName("ADMIN")).thenReturn(role);

        assertThrows(UserAlreadyHasRoleException.class, 
                () -> adminService.addRoleToUser("ADMIN", 1L));
        
        verify(userRepository, never()).save(any());
    }

    @Test
    void RemoveRoleFromUserShouldRemoveRoleSuccessfully() {
        user.getRoles().add(role);
        when(userQueryService.findUserById(1L)).thenReturn(user);
        when(roleQueryService.findRoleByName("ADMIN")).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.removeRoleFromUser("ADMIN", 1L);

        assertNotNull(result);
        assertFalse(user.getRoles().contains(role));
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void RemoveRoleFromUserShouldThrowExceptionWhenUserDoesntHaveRole() {
        when(userQueryService.findUserById(1L)).thenReturn(user);
        when(roleQueryService.findRoleByName("ADMIN")).thenReturn(role);

        assertThrows(UserDoesntHasRoleException.class, 
                () -> adminService.removeRoleFromUser("ADMIN", 1L));
        
        verify(userRepository, never()).save(any());
    }
}