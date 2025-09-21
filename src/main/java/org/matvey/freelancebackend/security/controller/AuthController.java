package org.matvey.freelancebackend.security.controller;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.jwt.JwtService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
}
