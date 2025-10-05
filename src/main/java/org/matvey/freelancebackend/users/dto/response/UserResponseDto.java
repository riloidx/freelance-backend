package org.matvey.freelancebackend.users.dto.response;

import lombok.Builder;
import lombok.Data;
import org.matvey.freelancebackend.ads.entity.Ad;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String description;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private List<Ad> ads;
    private Set<String> roles;
}
