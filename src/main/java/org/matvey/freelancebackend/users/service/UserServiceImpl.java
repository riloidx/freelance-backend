package org.matvey.freelancebackend.users.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserAlreadyExistsException;
import org.matvey.freelancebackend.users.exception.UserNotFoundException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.api.UserCommandService;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserCommandService, UserQueryService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> findAllUsersDto() {
        List<User> users = userRepo.findAll();

        return userMapper.toDto(users);
    }

    @Override
    public User findUserById(long id) {
        return userRepo.findById(id).
                orElseThrow(() -> new UserNotFoundException("id", String.valueOf(id)));
    }

    @Override
    public UserResponseDto findUserDtoById(long id) {
        User user = findUserById(id);

        return userMapper.toDto(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("email", email));
    }

    @Override
    public UserResponseDto findUserDtoByEmail(String email) {
        return userMapper.toDto(findUserByEmail(email));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException("username", username));
    }

    @Override
    public UserResponseDto findUserDtoByUsername(String username) {
        User user = findUserByUsername(username);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public User create(RegistrationDto dto) {
        existsByUsernameOrThrow(dto.getUsername());
        existsByEmailOrThrow(dto.getEmail());

        User user = userMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        return userRepo.save(user);
    }

    @Override
    @Transactional
    public UserResponseDto update(long id, UpdateUserDto dto) {
        User user = findUserById(id);

        userMapper.updateEntityFromDto(dto, user);

        User saved = userRepo.save(user);
        return userMapper.toDto(saved);
    }


    @Override
    @Transactional
    public void delete(long id) {
        User user = userRepo.findById(id).
                orElseThrow(() -> new UserNotFoundException("id", String.valueOf(id)));
        userRepo.delete(user);
    }

    private void existsByUsernameOrThrow(String username) {
        if (userRepo.existsByUsername(username)) {
            throw new UserAlreadyExistsException("username", username);
        }
    }

    private void existsByEmailOrThrow(String email) {
        if (userRepo.existsByEmail(email)) {
            throw new UserAlreadyExistsException("email", email);
        }
    }
}