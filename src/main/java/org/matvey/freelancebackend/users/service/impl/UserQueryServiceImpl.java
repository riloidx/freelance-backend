package org.matvey.freelancebackend.users.service.impl;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final LocalizationUtil localizationUtil;

    @Override
    public Page<UserResponseDto> findAllUsersDto(Pageable pageable) {
        Page<User> users = userRepo.findAll(pageable);

        return userMapper.toDto(users);
    }

    @Override
    public User findUserById(long id) {
        return userRepo.findById(id).
                orElseThrow(() -> new UserNotFoundException(localizationUtil.getMessage("error.user.not.found", "id", String.valueOf(id))));
    }

    @Override
    public UserResponseDto findUserDtoById(long id) {
        User user = findUserById(id);

        return userMapper.toDto(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException(localizationUtil.getMessage("error.user.not.found", "email", email)));
    }

    @Override
    public UserResponseDto findUserDtoByEmail(String email) {
        return userMapper.toDto(findUserByEmail(email));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException(localizationUtil.getMessage("error.user.not.found", "username", username)));
    }

    @Override
    public UserResponseDto findUserDtoByUsername(String username) {
        User user = findUserByUsername(username);

        return userMapper.toDto(user);
    }
}
