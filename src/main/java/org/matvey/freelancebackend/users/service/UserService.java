package org.matvey.freelancebackend.users.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.dto.request.RegisterDto;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserAlreadyExistsException;
import org.matvey.freelancebackend.users.exception.UserNotFoundException;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Transactional
    public UserResponseDto create(RegisterDto dto) {
        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("username", dto.getUsername());
        }
        if (dto.getEmail() != null && userRepo.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("email", dto.getEmail());
        }


        User user = userMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));


        User saved = userRepo.save(user);
        return userMapper.toResponseDto(saved);
    }


    @Transactional
    public UserResponseDto update(Long id, UpdateUserDto dto) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateEntityFromDto(dto, user);


        User saved = userRepo.save(user);
        return userMapper.toResponseDto(saved);
    }


    @Transactional
    public void delete(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepo.delete(user);
    }
}