package org.matvey.freelancebackend.contracts.service.api;

import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ContractQueryService {
    Page<ContractResponseDto> findMyContracts(Pageable pageable,
                                              ContractStatus contractStatus,
                                              Authentication auth);

    Contract findById(long id);

}
