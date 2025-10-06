package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;

public interface UserCommandService {

    User create(RegistrationDto dto);

    UserResponseDto update(long id, UpdateUserDto dto);

    void delete(long id);
}