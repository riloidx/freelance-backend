package org.matvey.freelancebackend.security.service;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userService.findUserByEmail(email);
        return new CustomUserDetails(user);
    }
}
