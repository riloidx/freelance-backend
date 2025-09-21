package org.matvey.freelancebackend.users.dto.response;

import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.roles.entity.Role;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String description;
    private String name;
    private String passwordHash;
    private Instant createdAt;
    private Instant updatedAt;
    private List<Ad> ads;
    private Set<Role> roles;
}
