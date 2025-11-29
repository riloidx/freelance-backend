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

import java.math.BigDecimal;
import org.matvey.freelancebackend.users.dto.request.WithdrawBalanceDto;
import org.matvey.freelancebackend.users.exception.InsufficientBalanceException;

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
    private UserProfileResponseDto userProfileResponseDto;
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

        userProfileResponseDto = new UserProfileResponseDto();
        userProfileResponseDto.setId(1L);
        userProfileResponseDto.setUsername("testuser");
        userProfileResponseDto.setEmail("test@example.com");

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(1L);
        userUpdateDto.setName("updateduser");
    }

    @Test
    void GetUserProfileShouldReturnUserProfileResponseDto() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(userMapper.toProfileDto(user)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto result = userProfileService.getUserProfile(authentication);

        assertNotNull(result);
        assertEquals(userProfileResponseDto.getId(), result.getId());
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

    @Test
    void WithdrawBalanceShouldReturnUpdatedUser() {
        user.setBalance(BigDecimal.valueOf(100.00));
        WithdrawBalanceDto withdrawDto = new WithdrawBalanceDto();
        withdrawDto.setAmount(BigDecimal.valueOf(50.00));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);
        when(userMapper.toProfileDto(user)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto result = userProfileService.withdrawBalance(authentication, withdrawDto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50.00), user.getBalance());
        verify(userRepo).save(user);
        verify(userMapper).toProfileDto(user);
    }

    @Test
    void WithdrawBalanceShouldThrowExceptionWhenInsufficientBalance() {
        user.setBalance(BigDecimal.valueOf(30.00));
        WithdrawBalanceDto withdrawDto = new WithdrawBalanceDto();
        withdrawDto.setAmount(BigDecimal.valueOf(50.00));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);

        assertThrows(InsufficientBalanceException.class,
                () -> userProfileService.withdrawBalance(authentication, withdrawDto));

        verify(userRepo, never()).save(any());
    }

    @Test
    void AddBalanceShouldIncreaseUserBalance() {
        user.setBalance(BigDecimal.valueOf(50.00));
        BigDecimal amountToAdd = BigDecimal.valueOf(25.00);

        when(userQueryService.findUserById(1L)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);

        userProfileService.addBalance(1L, amountToAdd);

        assertEquals(BigDecimal.valueOf(75.00), user.getBalance());
        verify(userRepo).save(user);
    }
}