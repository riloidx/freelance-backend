package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;

import java.util.List;

public interface UserQueryService {

    List<UserResponseDto> findAllUsersDto();

    UserResponseDto findUserDtoById(long id);

    UserResponseDto findUserDtoByEmail(String email);

    UserResponseDto findUserDtoByUsername(String username);

    User findUserById(long id);

    User findUserByEmail(String email);

    User findUserByUsername(String username);
}