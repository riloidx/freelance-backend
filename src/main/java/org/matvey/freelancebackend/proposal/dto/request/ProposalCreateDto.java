package org.matvey.freelancebackend.proposal.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProposalCreateDto {
    @NotNull(message = "Price is required")
    @DecimalMin(value = "10.00", message = "Minimal value is 10.00")
    @Digits(integer = 10, fraction = 2, message = "Format must be 10.00")
    private BigDecimal price;

    @Size(min = 10, max = 2000, message = "Message size must be from 10 to 2000 characters")
    private String message;

    @NotNull(message = "Ad id is required")
    private Long adId;

    @NotNull(message = "User id is required")
    private Long freelancerId;
}
