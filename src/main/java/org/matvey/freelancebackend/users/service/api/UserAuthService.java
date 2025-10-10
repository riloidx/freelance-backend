package org.matvey.freelancebackend.users.service.api;

import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.entity.User;

public interface UserAuthService {
    User createUser(RegistrationDto registrationDto);
}
