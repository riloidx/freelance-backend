package org.matvey.freelancebackend.users.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.security.exception.UnauthorizedException;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.dto.request.UserUpdateDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.dto.response.UserProfileResponseDto;
import org.matvey.freelancebackend.users.dto.request.WithdrawBalanceDto;
import org.matvey.freelancebackend.users.dto.request.DepositBalanceDto;
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

@Slf4j
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
        log.debug("Updating user profile for user id: {}", dto.getId());
        try {
            User user = prepareVerificatedUser(dto.getId(), authentication);
            userMapper.updateEntityFromDto(dto, user);
            User saved = userRepo.save(user);
            log.info("Successfully updated user profile for user id: {}", saved.getId());
            return userMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Error updating user profile for user id: {}", dto.getId(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteUserProfile(long id, Authentication authentication) {
        log.debug("Deleting user profile for user id: {}", id);
        try {
            userQueryService.findUserById(id);
            User user = prepareVerificatedUser(id, authentication);
            userRepo.delete(user);
            log.info("Successfully deleted user profile for user id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting user profile for user id: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public UserProfileResponseDto withdrawBalance(Authentication authentication, WithdrawBalanceDto withdrawDto) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).user();
        log.debug("Withdrawing balance {} for user id: {}", withdrawDto.getAmount(), user.getId());
        
        try {
            if (user.getBalance().compareTo(withdrawDto.getAmount()) < 0) {
                log.warn("Insufficient balance for withdrawal. User id: {}, Required: {}, Available: {}", 
                         user.getId(), withdrawDto.getAmount(), user.getBalance());
                throw new InsufficientBalanceException("Insufficient balance. Current balance: " + user.getBalance());
            }
            
            BigDecimal oldBalance = user.getBalance();
            user.setBalance(user.getBalance().subtract(withdrawDto.getAmount()));
            User savedUser = userRepo.save(user);
            log.info("Successfully withdrew balance for user id: {}. Old: {}, New: {}", user.getId(), oldBalance, savedUser.getBalance());
            return userMapper.toProfileDto(savedUser);
        } catch (Exception e) {
            log.error("Error withdrawing balance for user id: {}", user.getId(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public UserProfileResponseDto depositBalance(Authentication authentication, DepositBalanceDto depositDto) {
        User user = ((CustomUserDetails) authentication.getPrincipal()).user();
        log.debug("Depositing balance {} for user id: {}", depositDto.getAmount(), user.getId());
        
        try {
            BigDecimal oldBalance = user.getBalance();
            user.setBalance(user.getBalance().add(depositDto.getAmount()));
            User savedUser = userRepo.save(user);
            log.info("Successfully deposited balance for user id: {}. Old: {}, New: {}", user.getId(), oldBalance, savedUser.getBalance());
            return userMapper.toProfileDto(savedUser);
        } catch (Exception e) {
            log.error("Error depositing balance for user id: {}", user.getId(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void addBalance(Long userId, BigDecimal amount) {
        log.debug("Adding balance {} to user id: {}", amount, userId);
        try {
            User user = userQueryService.findUserById(userId);
            BigDecimal oldBalance = user.getBalance();
            user.setBalance(user.getBalance().add(amount));
            userRepo.save(user);
            log.info("Successfully added balance to user id: {}. Old: {}, New: {}", userId, oldBalance, user.getBalance());
        } catch (Exception e) {
            log.error("Error adding balance to user id: {}", userId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void subtractBalance(Long userId, BigDecimal amount) {
        log.debug("Subtracting balance {} from user id: {}", amount, userId);
        try {
            User user = userQueryService.findUserById(userId);
            
            if (user.getBalance().compareTo(amount) < 0) {
                log.warn("Insufficient balance for user id: {}. Required: {}, Available: {}", userId, amount, user.getBalance());
                throw new InsufficientBalanceException("Insufficient balance. Current balance: " + user.getBalance());
            }
            
            BigDecimal oldBalance = user.getBalance();
            user.setBalance(user.getBalance().subtract(amount));
            userRepo.save(user);
            log.info("Successfully subtracted balance from user id: {}. Old: {}, New: {}", userId, oldBalance, user.getBalance());
        } catch (Exception e) {
            log.error("Error subtracting balance from user id: {}", userId, e);
            throw e;
        }
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
