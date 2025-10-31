package org.matvey.freelancebackend.contracts.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.mapper.ContractMapper;
import org.matvey.freelancebackend.contracts.repository.ContractRepository;
import org.matvey.freelancebackend.contracts.service.api.ContractQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractQueryServiceImpl implements ContractQueryService {
    private final ContractRepository repository;
    private final ContractMapper contractMapper;

    @Override
    public Page<Contract> findMyContracts(Authentication auth, Pageable pageable, ContractStatus contractStatus) {
        return ;
    }
}
