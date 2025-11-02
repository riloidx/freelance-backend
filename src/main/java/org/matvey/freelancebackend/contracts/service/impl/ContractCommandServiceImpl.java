package org.matvey.freelancebackend.contracts.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.mapper.ContractMapper;
import org.matvey.freelancebackend.contracts.repository.ContractRepository;
import org.matvey.freelancebackend.contracts.service.api.ContractCommandService;
import org.matvey.freelancebackend.contracts.service.util.ContractSecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractCommandServiceImpl implements ContractCommandService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final ContractSecurityUtil contractSecurityUtil;

    @Override
    public ContractResponseDto accept(long contractId, Authentication auth) {
        return updateStatusIfOwner(contractId, ContractStatus.APPROVED, auth);
    }

    @Override
    public ContractResponseDto reject(long contractId, Authentication auth) {
        return updateStatusIfOwner(contractId, ContractStatus.REJECTED, auth);
    }

    private ContractResponseDto updateStatusIfOwner(long contractId,
                                                    ContractStatus status,
                                                    Authentication auth) {
        var contract = contractSecurityUtil.getContractIfOwner(contractId, auth);
        contract.setContractStatus(status);
        return contractMapper.toDto(contractRepository.save(contract));
    }
}
