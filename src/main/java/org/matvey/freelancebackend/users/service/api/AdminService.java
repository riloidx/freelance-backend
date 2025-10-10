package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.users.dto.response.UserResponseDto;

public interface AdminService {
    UserResponseDto addRoleToUser(String roleName, long userId);

    UserResponseDto removeRoleFromUser(String roleName, long userId);
}
