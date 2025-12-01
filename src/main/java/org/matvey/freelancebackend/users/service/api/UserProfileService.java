package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UserUpdateDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.dto.response.UserProfileResponseDto;
import org.matvey.freelancebackend.users.dto.request.WithdrawBalanceDto;
import org.matvey.freelancebackend.users.dto.request.DepositBalanceDto;
import org.matvey.freelancebackend.users.entity.User;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

public interface UserProfileService {

    UserProfileResponseDto getUserProfile(Authentication authentication);

    UserResponseDto updateUserProfile(Authentication authentication, UserUpdateDto userResponseDto);

    void deleteUserProfile(long id, Authentication authentication);

    UserProfileResponseDto withdrawBalance(Authentication authentication, WithdrawBalanceDto withdrawDto);

    UserProfileResponseDto depositBalance(Authentication authentication, DepositBalanceDto depositDto);

    void addBalance(Long userId, BigDecimal amount);
}
