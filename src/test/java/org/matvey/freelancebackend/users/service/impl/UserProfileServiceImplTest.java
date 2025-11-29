package org.matvey.freelancebackend.users.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.security.exception.UnauthorizedException;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.dto.request.UserUpdateDto;
import org.matvey.freelancebackend.users.dto.response.UserProfileResponseDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails userDetails;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private User user;
    private UserResponseDto userResponseDto;
    private UserUpdateDto userUpdateDto;

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

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(1L);
        userUpdateDto.setName("updateduser");
    }

    @Test
    void GetUserProfileShouldReturnUserResponseDto() {
        UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.builder().
                id(1L).
                build();


        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(userMapper.toProfileDto(user)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto result = userProfileService.getUserProfile(authentication);

        assertNotNull(result);
        assertEquals(userResponseDto.getId(), result.getId());
    }

    @Test
    void UpdateUserProfileShouldReturnUpdatedUser() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userProfileService.updateUserProfile(authentication, userUpdateDto);

        assertNotNull(result);
        verify(userMapper).updateEntityFromDto(userUpdateDto, user);
        verify(userRepo).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void UpdateUserProfileShouldThrowExceptionWhenUserIdMismatch() {
        User differentUser = new User();
        differentUser.setId(2L);

        userUpdateDto.setId(2L);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);

        assertThrows(UnauthorizedException.class,
                () -> userProfileService.updateUserProfile(authentication, userUpdateDto));

        verify(userRepo, never()).save(any());
    }

    @Test
    void DeleteUserProfileShouldDeleteUser() {
        when(userQueryService.findUserById(1L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);

        userProfileService.deleteUserProfile(1L, authentication);

        verify(userRepo).delete(user);
    }

    @Test
    void DeleteUserProfileShouldThrowExceptionWhenUserIdMismatch() {
        when(userQueryService.findUserById(2L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);

        assertThrows(UnauthorizedException.class,
                () -> userProfileService.deleteUserProfile(2L, authentication));

        verify(userRepo, never()).delete(any());
    }
}