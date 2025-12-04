package org.matvey.freelancebackend.users.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserNotFoundException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final LocalizationUtil localizationUtil;

    @Override
    public Page<UserResponseDto> findAllUsersDto(Pageable pageable) {
        log.debug("Finding all users with pagination");
        try {
            Page<User> users = userRepo.findAll(pageable);
            log.info("Found {} users", users.getTotalElements());
            return userMapper.toDto(users);
        } catch (Exception e) {
            log.error("Error finding all users", e);
            throw e;
        }
    }

    @Override
    public User findUserById(long id) {
        log.debug("Finding user by id: {}", id);
        try {
            User user = userRepo.findById(id).
                    orElseThrow(() -> {
                        log.warn("User not found with id: {}", id);
                        return new UserNotFoundException(localizationUtil.getMessage("error.user.not.found", "id", String.valueOf(id)));
                    });
            log.debug("Found user with id: {}", id);
            return user;
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding user by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public UserResponseDto findUserDtoById(long id) {
        log.debug("Finding user DTO by id: {}", id);
        User user = findUserById(id);
        return userMapper.toDto(user);
    }

    @Override
    public User findUserByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        try {
            User user = userRepo.findByEmail(email).
                    orElseThrow(() -> {
                        log.warn("User not found with email: {}", email);
                        return new UserNotFoundException(localizationUtil.getMessage("error.user.not.found", "email", email));
                    });
            log.debug("Found user with email: {}", email);
            return user;
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding user by email: {}", email, e);
            throw e;
        }
    }

    @Override
    public UserResponseDto findUserDtoByEmail(String email) {
        log.debug("Finding user DTO by email: {}", email);
        return userMapper.toDto(findUserByEmail(email));
    }

    @Override
    public User findUserByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        try {
            User user = userRepo.findByUsername(username).
                    orElseThrow(() -> {
                        log.warn("User not found with username: {}", username);
                        return new UserNotFoundException(localizationUtil.getMessage("error.user.not.found", "username", username));
                    });
            log.debug("Found user with username: {}", username);
            return user;
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding user by username: {}", username, e);
            throw e;
        }
    }

    @Override
    public UserResponseDto findUserDtoByUsername(String username) {
        log.debug("Finding user DTO by username: {}", username);
        User user = findUserByUsername(username);
        return userMapper.toDto(user);
    }
}
