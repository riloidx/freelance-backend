package org.matvey.freelancebackend.contracts.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.exception.ContractNotFoundException;
import org.matvey.freelancebackend.contracts.mapper.ContractMapper;
import org.matvey.freelancebackend.contracts.repository.ContractRepository;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractQueryServiceImplTest {

    @Mock
    private ContractRepository contractRepo;
    
    @Mock
    private ContractMapper contractMapper;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private CustomUserDetails userDetails;
    
    @InjectMocks
    private ContractQueryServiceImpl contractQueryService;
    
    private Contract contract;
    private ContractResponseDto contractResponseDto;
    private User user;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        
        contract = new Contract();
        contract.setId(1L);
        contract.setContractStatus(ContractStatus.PENDING);
        
        contractResponseDto = new ContractResponseDto();
        contractResponseDto.setId(1L);
        contractResponseDto.setContractStatus(String.valueOf(ContractStatus.PENDING));
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void FindMyContractsShouldReturnPageOfContracts() {
        Page<Contract> contractPage = new PageImpl<>(List.of(contract));
        Page<ContractResponseDto> expectedPage = new PageImpl<>(List.of(contractResponseDto));
        
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(contractRepo.findContractsByUserIdAndProposalStatus(1L, ContractStatus.PENDING, pageable))
                .thenReturn(contractPage);
        when(contractMapper.toDto(contractPage)).thenReturn(expectedPage);

        Page<ContractResponseDto> result = contractQueryService.findMyContracts(
                pageable, ContractStatus.PENDING, authentication);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(contractRepo).findContractsByUserIdAndProposalStatus(1L, ContractStatus.PENDING, pageable);
    }

    @Test
    void FindByIdShouldReturnContract() {
        when(contractRepo.findById(1L)).thenReturn(Optional.of(contract));

        Contract result = contractQueryService.findById(1L);

        assertNotNull(result);
        assertEquals(contract.getId(), result.getId());
        verify(contractRepo).findById(1L);
    }

    @Test
    void FindByIdShouldThrowExceptionWhenNotFound() {
        when(contractRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContractNotFoundException.class, 
                () -> contractQueryService.findById(1L));
        
        verify(contractRepo).findById(1L);
    }
}