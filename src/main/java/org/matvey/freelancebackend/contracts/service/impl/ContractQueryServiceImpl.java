package org.matvey.freelancebackend.contracts.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.exception.ContractNotFoundException;
import org.matvey.freelancebackend.contracts.mapper.ContractMapper;
import org.matvey.freelancebackend.contracts.repository.ContractRepository;
import org.matvey.freelancebackend.contracts.service.api.ContractQueryService;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractQueryServiceImpl implements ContractQueryService {
    private final ContractRepository contractRepo;
    private final ContractMapper contractMapper;

    @Override
    public Page<ContractResponseDto> findMyContracts(Pageable pageable,
                                                     ContractStatus contractStatus,
                                                     Authentication auth) {

        long userId = ((CustomUserDetails) auth.getPrincipal()).user().getId();
        Page<Contract> contracts = contractRepo.findContractsByUserIdAndProposalStatus(userId, contractStatus, pageable);

        return contractMapper.toDto(contracts);
    }

    @Override
    public Contract findById(long id) {
        return contractRepo.findById(id).
                orElseThrow(() -> new ContractNotFoundException("id", String.valueOf(id)));
    }
}
