package org.matvey.freelancebackend.users.dto.response;

import lombok.Builder;
import lombok.Data;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;

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
    private List<AdResponseDto> ads;
    private Set<String> roles;
}
