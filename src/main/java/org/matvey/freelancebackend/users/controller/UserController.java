package org.matvey.freelancebackend.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.service.api.UserCommandService;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping
    public ResponseEntity<?> findAll(
            @PageableDefault(size = 2, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<UserResponseDto> users = userQueryService.findAllUsersDto(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        UserResponseDto user = userQueryService.findUserDtoById(id);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(Authentication authentication) {
        UserResponseDto user = userQueryService.findUserDtoByEmail(authentication.getName());

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/me")
    public ResponseEntity<UserResponseDto> update(Authentication authentication,
                                                  @RequestBody @Valid UpdateUserDto updateUserDto) {
        UserResponseDto updatedUser = userCommandService.update(authentication.getName(), updateUserDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
}
