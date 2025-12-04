package org.matvey.freelancebackend.users.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.service.api.RoleQueryService;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserAlreadyHasRoleException;
import org.matvey.freelancebackend.users.exception.UserDoesntHasRoleException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.api.AdminService;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserQueryService userQueryService;
    private final RoleQueryService roleQueryService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto addRoleToUser(String roleName, long userId) {
        log.debug("Adding role {} to user id: {}", roleName, userId);
        try {
            UserResponseDto result = updateUserRole(roleName, userId, true);
            log.info("Successfully added role {} to user id: {}", roleName, userId);
            return result;
        } catch (Exception e) {
            log.error("Error adding role {} to user id: {}", roleName, userId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public UserResponseDto removeRoleFromUser(String roleName, long userId) {
        log.debug("Removing role {} from user id: {}", roleName, userId);
        try {
            UserResponseDto result = updateUserRole(roleName, userId, false);
            log.info("Successfully removed role {} from user id: {}", roleName, userId);
            return result;
        } catch (Exception e) {
            log.error("Error removing role {} from user id: {}", roleName, userId, e);
            throw e;
        }
    }

    private UserResponseDto updateUserRole(String roleName, long userId, boolean add) {
        User user = userQueryService.findUserById(userId);
        Role role = roleQueryService.findRoleByName(roleName);

        if (add) {
            ensureUserDoesNotHaveRole(user, role);
            user.getRoles().add(role);
        } else {
            ensureUserHasRole(user, role);
            user.getRoles().remove(role);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    private void ensureUserDoesNotHaveRole(User user, Role role) {
        if (user.getRoles().contains(role)) {
            log.warn("User with id={} already has role={}", user.getId(), role.getName());
            throw new UserAlreadyHasRoleException(
                    String.format("User with id=%d already has role=%s", user.getId(), role.getName()));
        }
    }

    private void ensureUserHasRole(User user, Role role) {
        if (!user.getRoles().contains(role)) {
            log.warn("User with id={} doesn't have role={}", user.getId(), role.getName());
            throw new UserDoesntHasRoleException(
                    String.format("User with id=%d doesn't have role=%s", user.getId(), role.getName()));
        }
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.debug("Getting all users with pagination");
        try {
            Page<User> users = userRepository.findAll(pageable);
            log.info("Found {} users", users.getTotalElements());
            return users.map(userMapper::toDto);
        } catch (Exception e) {
            log.error("Error getting all users", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        log.debug("Deleting user with id: {}", userId);
        try {
            User user = userQueryService.findUserById(userId);
            userRepository.delete(user);
            log.info("Successfully deleted user with id: {}", userId);
        } catch (Exception e) {
            log.error("Error deleting user with id: {}", userId, e);
            throw e;
        }
    }
}
