package org.matvey.freelancebackend.security.service;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.dto.request.LoginDto;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.security.dto.response.AuthResponseDto;
import org.matvey.freelancebackend.security.jwt.JwtUtil;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.service.api.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDto register(RegistrationDto registrationDto) {
        User user = userService.create(registrationDto);

        String token = jwtUtil.generateAccessToken(new CustomUserDetails(user));

        return new AuthResponseDto(userMapper.toDto(user), token);
    }

    public AuthResponseDto login(LoginDto loginDto) {
        User user = userService.findUserByEmail(loginDto.getEmail());
        matchPasswordOrThrow(user.getPasswordHash(), loginDto.getPassword());

        String token = jwtUtil.generateAccessToken(new CustomUserDetails(user));

        return new AuthResponseDto(userMapper.toDto(user), token);
    }

    private void matchPasswordOrThrow(String hashPassword, String password) {
        if (!passwordEncoder.matches(password, hashPassword)) {
            throw new RuntimeException("Invalid login or password");
        }
    }
}
