package org.matvey.freelancebackend.contracts.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.mapper.ContractMapper;
import org.matvey.freelancebackend.contracts.repository.ContractRepository;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.service.api.UserProfileService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractCommandServiceImplTest {

    @Mock
    private ContractRepository contractRepository;
    
    @Mock
    private ContractMapper contractMapper;
    
    @Mock
    private org.matvey.freelancebackend.contracts.service.api.ContractQueryService contractQueryService;
    
    @Mock
    private UserProfileService userProfileService;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private org.matvey.freelancebackend.security.user.CustomUserDetails userDetails;
    
    @InjectMocks
    private ContractCommandServiceImpl contractCommandService;
    
    private Contract contract;
    private ContractResponseDto contractResponseDto;
    private User freelancer;

    @BeforeEach
    void setUp() {
        freelancer = new User();
        freelancer.setId(1L);
        freelancer.setUsername("freelancer");
        
        contract = new Contract();
        contract.setId(1L);
        contract.setContractStatus(ContractStatus.IN_PROGRESS);
        contract.setFreelancer(freelancer);
        contract.setPrice(BigDecimal.valueOf(100.00));
        
        contractResponseDto = new ContractResponseDto();
        contractResponseDto.setId(1L);
        contractResponseDto.setContractStatus(ContractStatus.COMPLETED.name());
    }

    @Test
    void CompleteWorkShouldSetDeliveryUrlAndPendingReview() {
        contract.setContractStatus(ContractStatus.IN_PROGRESS);
        when(contractQueryService.findById(1L)).thenReturn(contract);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(freelancer);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toDto(contract)).thenReturn(contractResponseDto);

        ContractResponseDto result = contractCommandService.completeWork(1L, "http://delivery.url", authentication);

        assertNotNull(result);
        assertEquals(ContractStatus.PENDING_REVIEW, contract.getContractStatus());
        assertEquals("http://delivery.url", contract.getDeliveryUrl());
        verify(contractRepository).save(contract);
    }

    @Test
    void AcceptWorkShouldCompleteContractAndPayFreelancer() {
        User buyer = new User();
        buyer.setId(2L);
        contract.setBuyer(buyer);
        contract.setContractStatus(ContractStatus.PENDING_REVIEW);
        
        when(contractQueryService.findById(1L)).thenReturn(contract);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(buyer);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toDto(contract)).thenReturn(contractResponseDto);

        ContractResponseDto result = contractCommandService.acceptWork(1L, authentication);

        assertNotNull(result);
        assertEquals(ContractStatus.COMPLETED, contract.getContractStatus());
        verify(userProfileService).addBalance(freelancer.getId(), contract.getPrice());
        verify(contractRepository).save(contract);
    }

    @Test
    void RejectWorkShouldRejectContractAndRefundBuyer() {
        User buyer = new User();
        buyer.setId(2L);
        contract.setBuyer(buyer);
        contract.setContractStatus(ContractStatus.PENDING_REVIEW);
        
        when(contractQueryService.findById(1L)).thenReturn(contract);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(buyer);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toDto(contract)).thenReturn(contractResponseDto);

        ContractResponseDto result = contractCommandService.rejectWork(1L, authentication);

        assertNotNull(result);
        assertEquals(ContractStatus.REJECTED, contract.getContractStatus());
        verify(userProfileService).addBalance(buyer.getId(), contract.getPrice());
        verify(contractRepository).save(contract);
    }
}