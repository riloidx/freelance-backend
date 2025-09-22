package org.matvey.freelancebackend.security.controller;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.dto.request.LoginDto;
import org.matvey.freelancebackend.security.dto.request.RegisterDto;
import org.matvey.freelancebackend.security.dto.response.AuthResponseDto;
import org.matvey.freelancebackend.security.dto.response.RefreshResponseDto;
import org.matvey.freelancebackend.security.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> registration(@RequestBody RegisterDto registerDto) {

    }

    @PostMapping("/registration")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {

    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(@RequestBody RefreshResponseDto refreshResponseDto) {

    }
}
