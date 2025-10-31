package org.matvey.freelancebackend.contracts.dto.response;

import lombok.Data;
import org.matvey.freelancebackend.proposal.entity.Proposal;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ContractResponseDto {
    private long id;
    private BigDecimal price;
    private String contractStatus;
    private Instant createdAt;
    private Proposal proposal;
}
