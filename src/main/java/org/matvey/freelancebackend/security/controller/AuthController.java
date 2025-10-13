package org.matvey.freelancebackend.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.dto.request.LoginDto;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.security.dto.response.AuthResponseDto;
import org.matvey.freelancebackend.security.service.api.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<AuthResponseDto> registration(@Valid @RequestBody RegistrationDto registrationDto) {
        AuthResponseDto response = authService.register(registrationDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        AuthResponseDto response = authService.login(loginDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
