package org.matvey.freelancebackend.users.controller.admin;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.service.api.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PutMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<UserResponseDto> addRoleToUser(@PathVariable long userId,
                                                         @PathVariable String roleName) {
        UserResponseDto userResponseDto = adminService.addRoleToUser(roleName, userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<UserResponseDto> removeRoleFromUser(@PathVariable long userId,
                                                              @PathVariable String roleName) {
        UserResponseDto userResponseDto = adminService.removeRoleFromUser(roleName, userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }
}
