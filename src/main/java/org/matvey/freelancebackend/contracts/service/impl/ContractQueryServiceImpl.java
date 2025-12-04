package org.matvey.freelancebackend.contracts.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.debug("Finding contracts for user: {} with status: {}", userId, contractStatus);
        try {
            Page<Contract> contracts = contractRepo.findContractsByUserIdAndStatus(userId, contractStatus, pageable);
            log.info("Found {} contracts for user: {}", contracts.getTotalElements(), userId);
            return contractMapper.toDto(contracts);
        } catch (Exception e) {
            log.error("Error finding contracts for user: {}", userId, e);
            throw e;
        }
    }

    @Override
    public Contract findById(long id) {
        log.debug("Finding contract by id: {}", id);
        try {
            Contract contract = contractRepo.findById(id).
                    orElseThrow(() -> {
                        log.warn("Contract not found with id: {}", id);
                        return new ContractNotFoundException("id", String.valueOf(id));
                    });
            log.debug("Found contract with id: {}", id);
            return contract;
        } catch (ContractNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding contract by id: {}", id, e);
            throw e;
        }
    }
}
