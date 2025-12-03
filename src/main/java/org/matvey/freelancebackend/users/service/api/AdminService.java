package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    UserResponseDto addRoleToUser(String roleName, long userId);

    UserResponseDto removeRoleFromUser(String roleName, long userId);

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    void deleteUser(long userId);
}
