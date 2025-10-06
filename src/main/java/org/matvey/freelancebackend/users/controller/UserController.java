package org.matvey.freelancebackend.users.controller;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserQueryService userQueryService;

    @GetMapping("/me")
    public UserResponseDto me(Authentication authentication) {
        return userQueryService.findUserDtoByEmail(authentication.getName());
    }
}
