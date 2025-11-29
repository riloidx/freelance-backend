package org.matvey.freelancebackend.users.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawBalanceDto {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "10.00", message = "Amount must be greater than 10")
    private BigDecimal amount;
}