package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UserUpdateDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.springframework.security.core.Authentication;

public interface UserProfileService {

    UserResponseDto getUserProfile(Authentication authentication);

    UserResponseDto updateUserProfile(Authentication authentication, UserUpdateDto userResponseDto);

    void deleteUserProfile(long id, Authentication authentication);
}
