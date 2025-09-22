package org.matvey.freelancebackend.users.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.dto.request.RegisterDto;
import org.matvey.freelancebackend.security.service.PasswordService;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserAlreadyExistsException;
import org.matvey.freelancebackend.users.exception.UserNotFoundException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordService passwordService;
    private final UserMapper userMapper;


    @Transactional
    public UserResponseDto create(RegisterDto dto) {
        existsByUsernameOrThrow(dto.getUsername());
        existsByEmailOrThrow(dto.getEmail());

        User user = userMapper.toEntity(dto);
        user.setPasswordHash(passwordService.encodePassword(dto.getPassword()));


        User saved = userRepo.save(user);
        return userMapper.toDto(saved);
    }


    @Transactional
    public UserResponseDto update(Long id, UpdateUserDto dto) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateEntityFromDto(dto, user);

        User saved = userRepo.save(user);
        return userMapper.toDto(saved);
    }


    @Transactional
    public void delete(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
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