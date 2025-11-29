package org.matvey.freelancebackend.users.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.users.dto.request.UserUpdateDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.dto.response.UserProfileResponseDto;
import org.matvey.freelancebackend.users.dto.request.WithdrawBalanceDto;
import org.matvey.freelancebackend.users.service.api.UserProfileService;
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
    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> me(Authentication authentication) {
        UserProfileResponseDto user = userProfileService.getUserProfile(authentication);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> update(Authentication authentication,
                                                  @RequestBody @Valid UserUpdateDto userUpdateDto) {
        UserResponseDto updatedUser = userProfileService.updateUserProfile(authentication, userUpdateDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<UserResponseDto> users = userQueryService.findAllUsersDto(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        UserResponseDto user = userQueryService.findUserDtoById(id);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, Authentication authentication) {
        userProfileService.deleteUserProfile(id, authentication);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/me/withdraw")
    public ResponseEntity<UserProfileResponseDto> withdrawBalance(Authentication authentication,
                                                                  @RequestBody @Valid WithdrawBalanceDto withdrawDto) {
        UserProfileResponseDto user = userProfileService.withdrawBalance(authentication, withdrawDto);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
