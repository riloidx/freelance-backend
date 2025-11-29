package org.matvey.freelancebackend.users.service.impl;


import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.exception.UnauthorizedException;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.dto.request.UserUpdateDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.dto.response.UserProfileResponseDto;
import org.matvey.freelancebackend.users.dto.request.WithdrawBalanceDto;
import org.matvey.freelancebackend.users.exception.InsufficientBalanceException;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.api.UserProfileService;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final UserQueryService userQueryService;

    @Override
    public UserProfileResponseDto getUserProfile(Authentication authentication) {
        User curUser = ((CustomUserDetails) authentication.getPrincipal()).user();

        return userMapper.toProfileDto(curUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserProfile(Authentication authentication, UserUpdateDto dto) {
        User user = prepareVerificatedUser(dto.getId(), authentication);

        userMapper.updateEntityFromDto(dto, user);

        User saved = userRepo.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteUserProfile(long id, Authentication authentication) {
        userQueryService.findUserById(id);
        User user = prepareVerificatedUser(id, authentication);

        userRepo.delete(user);
    }

    @Override
    @Transactional
    public UserProfileResponseDto withdrawBalance(Authentication authentication, WithdrawBalanceDto withdrawDto) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).user();
        
        if (user.getBalance().compareTo(withdrawDto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance. Current balance: " + user.getBalance());
        }
        
        user.setBalance(user.getBalance().subtract(withdrawDto.getAmount()));
        User savedUser = userRepo.save(user);
        
        return userMapper.toProfileDto(savedUser);
    }

    @Override
    @Transactional
    public void addBalance(Long userId, BigDecimal amount) {
        User user = userQueryService.findUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        userRepo.save(user);
    }

    private User prepareVerificatedUser(long id, Authentication authentication) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).user();
        verifyUserById(user.getId(), id);

        return user;
    }

    private void verifyUserById(long factId, long curUserId) {
        if (factId != curUserId) {
            throw new UnauthorizedException("User with id=" + curUserId + " can't change data of user with id=" + factId);
        }
    }
}
