package org.matvey.freelancebackend.users.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserNotFoundException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepo;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private org.matvey.freelancebackend.common.util.LocalizationUtil localizationUtil;
    
    @InjectMocks
    private UserQueryServiceImpl userQueryService;
    
    private User user;
    private UserResponseDto userResponseDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setUsername("testuser");
        userResponseDto.setEmail("test@example.com");
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void FindAllUsersDtoShouldReturnPageOfUsers() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        Page<UserResponseDto> expectedPage = new PageImpl<>(List.of(userResponseDto));
        
        when(userRepo.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toDto(userPage)).thenReturn(expectedPage);

        Page<UserResponseDto> result = userQueryService.findAllUsersDto(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepo).findAll(pageable);
        verify(userMapper).toDto(userPage);
    }

    @Test
    void FindUserByIdShouldReturnUser() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        User result = userQueryService.findUserById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(userRepo).findById(1L);
    }

    @Test
    void FindUserByIdShouldThrowExceptionWhenNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, 
                () -> userQueryService.findUserById(1L));
        
        assertNotNull(exception);
        verify(userRepo).findById(1L);
    }

    @Test
    void FindUserDtoByIdShouldReturnUserResponseDto() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userQueryService.findUserDtoById(1L);

        assertNotNull(result);
        assertEquals(userResponseDto.getId(), result.getId());
        verify(userRepo).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    void FindUserByEmailShouldReturnUser() {
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userQueryService.findUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepo).findByEmail("test@example.com");
    }

    @Test
    void FindUserByEmailShouldThrowExceptionWhenNotFound() {
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, 
                () -> userQueryService.findUserByEmail("test@example.com"));
        
        assertNotNull(exception);
        verify(userRepo).findByEmail("test@example.com");
    }

    @Test
    void FindUserDtoByEmailShouldReturnUserResponseDto() {
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userQueryService.findUserDtoByEmail("test@example.com");

        assertNotNull(result);
        assertEquals(userResponseDto.getEmail(), result.getEmail());
        verify(userRepo).findByEmail("test@example.com");
        verify(userMapper).toDto(user);
    }

    @Test
    void FindUserByUsernameShouldReturnUser() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = userQueryService.findUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepo).findByUsername("testuser");
    }

    @Test
    void FindUserByUsernameShouldThrowExceptionWhenNotFound() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, 
                () -> userQueryService.findUserByUsername("testuser"));
        
        assertNotNull(exception);
        verify(userRepo).findByUsername("testuser");
    }

    @Test
    void FindUserDtoByUsernameShouldReturnUserResponseDto() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userQueryService.findUserDtoByUsername("testuser");

        assertNotNull(result);
        assertEquals(userResponseDto.getUsername(), result.getUsername());
        verify(userRepo).findByUsername("testuser");
        verify(userMapper).toDto(user);
    }
}