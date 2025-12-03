package org.matvey.freelancebackend.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDto {
    private Long id;
    private String username;
    private String email;
    private String descriptionEn;
    private String descriptionRu;
    private String name;
    private BigDecimal balance;
    private Instant createdAt;
    private Set<String> roles;
}