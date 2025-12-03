package org.matvey.freelancebackend.users.service.impl;

import lombok.RequiredArgsConstructor;
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
        return updateUserRole(roleName, userId, true);
    }

    @Override
    @Transactional
    public UserResponseDto removeRoleFromUser(String roleName, long userId) {
        return updateUserRole(roleName, userId, false);
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
            throw new UserAlreadyHasRoleException(
                    String.format("User with id=%d already has role=%s", user.getId(), role.getName()));
        }
    }

    private void ensureUserHasRole(User user, Role role) {
        if (!user.getRoles().contains(role)) {
            throw new UserDoesntHasRoleException(
                    String.format("User with id=%d doesn't have role=%s", user.getId(), role.getName()));
        }
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        User user = userQueryService.findUserById(userId);
        userRepository.delete(user);
    }
}
