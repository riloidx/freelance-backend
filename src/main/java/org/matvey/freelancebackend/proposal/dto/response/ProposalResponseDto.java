package org.matvey.freelancebackend.proposal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalResponseDto {
    private Long id;
    private BigDecimal price;
    private String message;
    private String proposalStatus;
    private Instant createdAt;
    private AdResponseDto ad;
    private UserResponseDto user;
    private UserResponseDto buyer;
}
