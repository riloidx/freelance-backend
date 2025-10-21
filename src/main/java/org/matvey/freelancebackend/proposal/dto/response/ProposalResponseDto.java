package org.matvey.freelancebackend.proposal.dto.response;

import lombok.Builder;
import lombok.Data;
import org.matvey.freelancebackend.ads.dto.response.AdResponseDto;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class ProposalResponseDto {
    private Long id;
    private BigDecimal price;
    private String message;
    private ProposalStatus proposalStatus;
    private Instant createdAt;
    private AdResponseDto ad;
    private UserResponseDto user;
    private ContractResponseDto contract;
}
