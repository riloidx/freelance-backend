package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;

public interface UserService {

    User create(RegistrationDto dto);

    UserResponseDto update(long id, UpdateUserDto dto);

    User findUserByEmail(String email);

    UserResponseDto findUserDtoByEmail(String email);

    void delete(long id);
}
