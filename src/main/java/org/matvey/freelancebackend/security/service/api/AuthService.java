package org.matvey.freelancebackend.security.service.api;

import org.matvey.freelancebackend.security.dto.request.LoginDto;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.security.dto.response.AuthResponseDto;
import org.matvey.freelancebackend.users.entity.User;

public interface AuthService {
    AuthResponseDto login(LoginDto loginDto);

    AuthResponseDto register(RegistrationDto registrationDto);
}
