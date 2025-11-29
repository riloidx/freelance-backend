package org.matvey.freelancebackend.contracts.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.mapper.ContractMapper;
import org.matvey.freelancebackend.contracts.repository.ContractRepository;
import org.matvey.freelancebackend.contracts.service.util.ContractSecurityUtil;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.service.api.UserProfileService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class ContractCommandServiceImplTest {

    @Mock
    private ContractRepository contractRepository;
    
    @Mock
    private ContractMapper contractMapper;
    
    @Mock
    private ContractSecurityUtil contractSecurityUtil;
    
    @Mock
    private UserProfileService userProfileService;
    
    @Mock
    private Authentication authentication;
    
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
        contract.setContractStatus(ContractStatus.PENDING);
        contract.setFreelancer(freelancer);
        contract.setPrice(BigDecimal.valueOf(100.00));
        
        contractResponseDto = new ContractResponseDto();
        contractResponseDto.setId(1L);
        contractResponseDto.setContractStatus(ContractStatus.APPROVED.name());
    }

    @Test
    void AcceptShouldReturnApprovedContract() {
        when(contractSecurityUtil.getContractIfOwner(1L, authentication)).thenReturn(contract);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toDto(contract)).thenReturn(contractResponseDto);

        ContractResponseDto result = contractCommandService.accept(1L, authentication);

        assertNotNull(result);
        assertEquals(ContractStatus.APPROVED, contract.getContractStatus());
        verify(contractRepository).save(contract);
        verify(contractMapper).toDto(contract);
        verify(userProfileService).addBalance(freelancer.getId(), contract.getPrice());
    }

    @Test
    void RejectShouldReturnRejectedContract() {
        when(contractSecurityUtil.getContractIfOwner(1L, authentication)).thenReturn(contract);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toDto(contract)).thenReturn(contractResponseDto);

        ContractResponseDto result = contractCommandService.reject(1L, authentication);

        assertNotNull(result);
        assertEquals(ContractStatus.REJECTED, contract.getContractStatus());
        verify(contractRepository).save(contract);
        verify(contractMapper).toDto(contract);
        verify(userProfileService, never()).addBalance(any(), any());
    }
}