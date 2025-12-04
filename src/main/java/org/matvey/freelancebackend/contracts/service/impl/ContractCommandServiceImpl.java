package org.matvey.freelancebackend.contracts.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.debug("Completing work for contract id: {} by user: {}", contractId, userId);
        
        try {
            Contract contract = contractQueryService.findById(contractId);
            
            if (!contract.getFreelancer().getId().equals(userId)) {
                log.warn("User {} attempted to complete work for contract {} but is not the freelancer", userId, contractId);
                throw new ContractAccessDeniedException("Only freelancer can complete work");
            }
            
            if (contract.getContractStatus() != ContractStatus.IN_PROGRESS) {
                log.warn("Contract {} is not in progress, current status: {}", contractId, contract.getContractStatus());
                throw new IllegalStateException("Contract must be in progress");
            }
            
            contract.setDeliveryUrl(deliveryUrl);
            contract.setContractStatus(ContractStatus.PENDING_REVIEW);
            Contract saved = contractRepository.save(contract);
            log.info("Successfully completed work for contract id: {}", contractId);
            return contractMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Error completing work for contract id: {}", contractId, e);
            throw e;
        }
    }

    @Override
    public ContractResponseDto acceptWork(long contractId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.user().getId();
        log.debug("Accepting work for contract id: {} by user: {}", contractId, userId);
        
        try {
            Contract contract = contractQueryService.findById(contractId);
            
            if (!contract.getBuyer().getId().equals(userId)) {
                log.warn("User {} attempted to accept work for contract {} but is not the buyer", userId, contractId);
                throw new ContractAccessDeniedException("Only buyer can accept work");
            }
            
            if (contract.getContractStatus() != ContractStatus.PENDING_REVIEW) {
                log.warn("Contract {} is not pending review, current status: {}", contractId, contract.getContractStatus());
                throw new IllegalStateException("Contract must be pending review");
            }
            
            contract.setContractStatus(ContractStatus.COMPLETED);
            userProfileService.addBalance(contract.getFreelancer().getId(), contract.getPrice());
            Contract saved = contractRepository.save(contract);
            log.info("Successfully accepted work for contract id: {}, added balance to freelancer: {}", contractId, contract.getFreelancer().getId());
            return contractMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Error accepting work for contract id: {}", contractId, e);
            throw e;
        }
    }

    @Override
    public ContractResponseDto rejectWork(long contractId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.user().getId();
        log.debug("Rejecting work for contract id: {} by user: {}", contractId, userId);
        
        try {
            Contract contract = contractQueryService.findById(contractId);
            
            if (!contract.getBuyer().getId().equals(userId)) {
                log.warn("User {} attempted to reject work for contract {} but is not the buyer", userId, contractId);
                throw new ContractAccessDeniedException("Only buyer can reject work");
            }
            
            if (contract.getContractStatus() != ContractStatus.PENDING_REVIEW) {
                log.warn("Contract {} is not pending review, current status: {}", contractId, contract.getContractStatus());
                throw new IllegalStateException("Contract must be pending review");
            }
            
            contract.setContractStatus(ContractStatus.REJECTED);
            userProfileService.addBalance(contract.getBuyer().getId(), contract.getPrice());
            Contract saved = contractRepository.save(contract);
            log.info("Successfully rejected work for contract id: {}, refunded balance to buyer: {}", contractId, contract.getBuyer().getId());
            return contractMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Error rejecting work for contract id: {}", contractId, e);
            throw e;
        }
    }
}
