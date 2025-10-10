package org.matvey.freelancebackend.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserQueryService userQueryService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userQueryService.findUserByEmail(email);

        return new CustomUserDetails(user);
    }
}
