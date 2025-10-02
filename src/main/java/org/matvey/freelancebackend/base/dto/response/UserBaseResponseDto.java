package org.matvey.freelancebackend.base.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.roles.entity.Role;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder
public class UserBaseResponseDto {
    private Long id;
    private String username;
    private String email;
    private String description;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private List<Ad> ads; // change to dto
    private Set<Role> roles; // change to dto
}
