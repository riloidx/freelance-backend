package org.matvey.freelancebackend.users.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.service.api.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PutMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<UserResponseDto> addRoleToUser(@PathVariable long userId,
                                                         @PathVariable String roleName) {
        log.info("PUT /admin/{}/roles/{} - Adding role to user", userId, roleName);
        UserResponseDto userResponseDto = adminService.addRoleToUser(roleName, userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<UserResponseDto> removeRoleFromUser(@PathVariable long userId,
                                                              @PathVariable String roleName) {
        log.info("DELETE /admin/{}/roles/{} - Removing role from user", userId, roleName);
        UserResponseDto userResponseDto = adminService.removeRoleFromUser(roleName, userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        log.info("GET /admin/users - Getting all users");
        Page<UserResponseDto> users = adminService.getAllUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        log.info("DELETE /admin/users/{} - Deleting user", userId);
        adminService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
