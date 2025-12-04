package org.matvey.freelancebackend.users.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.service.api.RoleQueryService;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserAlreadyExistsException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.api.UserAuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepo;
    private final RoleQueryService roleQueryService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final LocalizationUtil localizationUtil;

    @Override
    @Transactional
    public User createUser(RegistrationDto dto) {
        log.debug("Creating user with username: {}, email: {}", dto.getUsername(), dto.getEmail());
        try {
            existsByUsernameOrThrow(dto.getUsername());
            existsByEmailOrThrow(dto.getEmail());
            User user = prepareUser(dto);
            User saved = userRepo.save(user);
            log.info("Successfully created user with id: {}, username: {}", saved.getId(), saved.getUsername());
            return saved;
        } catch (Exception e) {
            log.error("Error creating user with username: {}", dto.getUsername(), e);
            throw e;
        }
    }

    private User prepareUser(RegistrationDto dto) {
        User user = userMapper.toEntity(dto);

        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        Role role = roleQueryService.findRoleByName("USER");
        user.getRoles().add(role);

        return user;
    }

    public void existsByUsernameOrThrow(String username) {
        if (userRepo.existsByUsername(username)) {
            log.warn("User already exists with username: {}", username);
            throw new UserAlreadyExistsException(localizationUtil.getMessage("error.user.already.exists", "username", username));
        }
    }

    public void existsByEmailOrThrow(String email) {
        if (userRepo.existsByEmail(email)) {
            log.warn("User already exists with email: {}", email);
            throw new UserAlreadyExistsException(localizationUtil.getMessage("error.user.already.exists", "email", email));
        }
    }
}
