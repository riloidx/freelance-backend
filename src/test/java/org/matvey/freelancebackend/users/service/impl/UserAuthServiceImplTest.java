package org.matvey.freelancebackend.users.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.service.api.RoleQueryService;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserAlreadyExistsException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private UserRepository userRepo;
    
    @Mock
    private RoleQueryService roleQueryService;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private org.matvey.freelancebackend.common.util.LocalizationUtil localizationUtil;
    
    @InjectMocks
    private UserAuthServiceImpl userAuthService;
    
    private User user;
    private Role role;
    private RegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("USER");
        
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRoles(new HashSet<>());
        
        registrationDto = new RegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password");
    }

    @Test
    void CreateUserShouldReturnUser() {
        when(userRepo.existsByUsername("testuser")).thenReturn(false);
        when(userRepo.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(registrationDto)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(roleQueryService.findRoleByName("USER")).thenReturn(role);
        when(userRepo.save(any(User.class))).thenReturn(user);

        User result = userAuthService.createUser(registrationDto);

        assertNotNull(result);
        assertEquals("hashedPassword", result.getPasswordHash());
        assertTrue(result.getRoles().contains(role));
        verify(userRepo).save(any(User.class));
    }

    @Test
    void CreateUserShouldThrowExceptionWhenUsernameExists() {
        when(userRepo.existsByUsername("testuser")).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistsException.class, 
                () -> userAuthService.createUser(registrationDto));
        
        assertNotNull(exception);
        verify(userRepo, never()).save(any());
    }

    @Test
    void CreateUserShouldThrowExceptionWhenEmailExists() {
        when(userRepo.existsByUsername("testuser")).thenReturn(false);
        when(userRepo.existsByEmail("test@example.com")).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistsException.class, 
                () -> userAuthService.createUser(registrationDto));
        
        assertNotNull(exception);
        verify(userRepo, never()).save(any());
    }

    @Test
    void ExistsByUsernameOrThrowShouldThrowExceptionWhenExists() {
        when(userRepo.existsByUsername("testuser")).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistsException.class, 
                () -> userAuthService.existsByUsernameOrThrow("testuser"));
        
        assertNotNull(exception);
    }

    @Test
    void ExistsByUsernameOrThrowShouldNotThrowWhenNotExists() {
        when(userRepo.existsByUsername("testuser")).thenReturn(false);

        assertDoesNotThrow(() -> userAuthService.existsByUsernameOrThrow("testuser"));
    }

    @Test
    void ExistsByEmailOrThrowShouldThrowExceptionWhenExists() {
        when(userRepo.existsByEmail("test@example.com")).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistsException.class, 
                () -> userAuthService.existsByEmailOrThrow("test@example.com"));
        
        assertNotNull(exception);
    }

    @Test
    void ExistsByEmailOrThrowShouldNotThrowWhenNotExists() {
        when(userRepo.existsByEmail("test@example.com")).thenReturn(false);

        assertDoesNotThrow(() -> userAuthService.existsByEmailOrThrow("test@example.com"));
    }
}