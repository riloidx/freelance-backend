package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryService {

    Page<UserResponseDto> findAllUsersDto(Pageable pageable);

    UserResponseDto findUserDtoById(long id);

    UserResponseDto findUserDtoByEmail(String email);

    UserResponseDto findUserDtoByUsername(String username);

    User findUserById(long id);

    User findUserByEmail(String email);

    User findUserByUsername(String username);
}