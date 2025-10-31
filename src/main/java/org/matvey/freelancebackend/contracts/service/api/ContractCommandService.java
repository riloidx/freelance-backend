package org.matvey.freelancebackend.contracts.service.api;

import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.springframework.security.core.Authentication;

public interface ContractCommandService {
    ContractResponseDto accept(long contractId, Authentication auth);
    ContractResponseDto reject(long contractId, Authentication auth);
}
