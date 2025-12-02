package org.matvey.freelancebackend.contracts.service.api;

import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.springframework.security.core.Authentication;

public interface ContractCommandService {
    ContractResponseDto completeWork(long contractId, String deliveryUrl, Authentication auth);
    ContractResponseDto acceptWork(long contractId, Authentication auth);
    ContractResponseDto rejectWork(long contractId, Authentication auth);
}
