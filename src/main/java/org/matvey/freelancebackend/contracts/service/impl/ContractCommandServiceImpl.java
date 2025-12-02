package org.matvey.freelancebackend.contracts.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.exception.ContractAccessDeniedException;
import org.matvey.freelancebackend.contracts.mapper.ContractMapper;
import org.matvey.freelancebackend.contracts.repository.ContractRepository;
import org.matvey.freelancebackend.contracts.service.api.ContractCommandService;
import org.matvey.freelancebackend.contracts.service.api.ContractQueryService;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.service.api.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractCommandServiceImpl implements ContractCommandService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final ContractQueryService contractQueryService;
    private final UserProfileService userProfileService;

    @Override
    public ContractResponseDto completeWork(long contractId, String deliveryUrl, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.user().getId();
        
        Contract contract = contractQueryService.findById(contractId);
        
        if (!contract.getFreelancer().getId().equals(userId)) {
            throw new ContractAccessDeniedException("Only freelancer can complete work");
        }
        
        if (contract.getContractStatus() != ContractStatus.IN_PROGRESS) {
            throw new IllegalStateException("Contract must be in progress");
        }
        
        contract.setDeliveryUrl(deliveryUrl);
        contract.setContractStatus(ContractStatus.PENDING_REVIEW);
        
        return contractMapper.toDto(contractRepository.save(contract));
    }

    @Override
    public ContractResponseDto acceptWork(long contractId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.user().getId();
        
        Contract contract = contractQueryService.findById(contractId);
        
        if (!contract.getBuyer().getId().equals(userId)) {
            throw new ContractAccessDeniedException("Only buyer can accept work");
        }
        
        if (contract.getContractStatus() != ContractStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Contract must be pending review");
        }
        
        contract.setContractStatus(ContractStatus.COMPLETED);
        userProfileService.addBalance(contract.getFreelancer().getId(), contract.getPrice());
        
        return contractMapper.toDto(contractRepository.save(contract));
    }

    @Override
    public ContractResponseDto rejectWork(long contractId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.user().getId();
        
        Contract contract = contractQueryService.findById(contractId);
        
        if (!contract.getBuyer().getId().equals(userId)) {
            throw new ContractAccessDeniedException("Only buyer can reject work");
        }
        
        if (contract.getContractStatus() != ContractStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Contract must be pending review");
        }
        
        contract.setContractStatus(ContractStatus.REJECTED);
        userProfileService.addBalance(contract.getBuyer().getId(), contract.getPrice());
        
        return contractMapper.toDto(contractRepository.save(contract));
    }
}
