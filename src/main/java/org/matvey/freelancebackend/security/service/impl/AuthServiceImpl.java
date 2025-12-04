package org.matvey.freelancebackend.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
import org.matvey.freelancebackend.security.dto.request.LoginDto;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.security.dto.response.AuthResponseDto;
import org.matvey.freelancebackend.security.exception.InvalidCredentialsException;
import org.matvey.freelancebackend.security.jwt.JwtUtil;
import org.matvey.freelancebackend.security.service.api.AuthService;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.UserNotFoundException;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.service.api.UserAuthService;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAuthService userAuthService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final LocalizationUtil localizationUtil;

    @Override
    public AuthResponseDto register(RegistrationDto registrationDto) {
        log.debug("Registration attempt for email: {}", registrationDto.getEmail());
        try {
            User user = userAuthService.createUser(registrationDto);
            String token = jwtUtil.generateAccessToken(new CustomUserDetails(user));
            log.info("User successfully registered: {}", registrationDto.getEmail());
            return new AuthResponseDto(userMapper.toDto(user), token);
        } catch (Exception e) {
            log.error("Error during registration for email: {}", registrationDto.getEmail(), e);
            throw e;
        }
    }

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        log.debug("Login attempt for email: {}", loginDto.getEmail());
        User user;
        try {
            user = userQueryService.findUserByEmail(loginDto.getEmail());
        } catch (UserNotFoundException e) {
            log.warn("Login failed: user not found with email: {}", loginDto.getEmail());
            throw new InvalidCredentialsException(localizationUtil.getMessage("error.invalid.credentials"));
        }
        
        try {
            matchPasswordOrThrow(user.getPasswordHash(), loginDto.getPassword());
            String token = jwtUtil.generateAccessToken(new CustomUserDetails(user));
            log.info("User successfully logged in: {}", loginDto.getEmail());
            return new AuthResponseDto(userMapper.toDto(user), token);
        } catch (InvalidCredentialsException e) {
            log.warn("Login failed: invalid password for email: {}", loginDto.getEmail());
            throw e;
        } catch (Exception e) {
            log.error("Error during login for email: {}", loginDto.getEmail(), e);
            throw e;
        }
    }

    private void matchPasswordOrThrow(String hashPassword, String password) {
        if (!passwordEncoder.matches(password, hashPassword)) {
            throw new InvalidCredentialsException(localizationUtil.getMessage("error.invalid.credentials"));
        }
    }
}
