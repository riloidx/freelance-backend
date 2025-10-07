package org.matvey.freelancebackend.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.service.api.UserCommandService;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(Authentication authentication) {
        UserResponseDto user = userQueryService.findUserDtoByEmail(authentication.getName());

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public ResponseEntity<UserResponseDto> update(Authentication authentication,
                                                  @RequestBody @Valid UpdateUserDto updateUserDto) {
        UserResponseDto updatedUser = userCommandService.update(authentication.getName(), updateUserDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
}
