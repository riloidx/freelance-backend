package org.matvey.freelancebackend.proposal.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.ads.service.util.AdSecurityUtil;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.exception.ProposalNotFoundException;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
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
class ProposalQueryServiceImplTest {

    @Mock
    private ProposalRepository proposalRepo;
    
    @Mock
    private ProposalMapper proposalMapper;
    
    @Mock
    private AdSecurityUtil adSecurityUtil;
    
    @Mock
    private org.matvey.freelancebackend.common.util.LocalizationUtil localizationUtil;
    
    @Mock
    private Authentication authentication;
    
    @InjectMocks
    private ProposalQueryServiceImpl proposalQueryService;
    
    private Proposal proposal;
    private ProposalResponseDto proposalResponseDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        proposal = new Proposal();
        proposal.setId(1L);
        proposal.setProposalStatus(ProposalStatus.PENDING);
        
        proposalResponseDto = new ProposalResponseDto();
        proposalResponseDto.setId(1L);
        proposalResponseDto.setProposalStatus(ProposalStatus.PENDING.name());
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void FindAllProposalsByAdIdShouldReturnPageOfProposals() {
        Page<Proposal> proposalPage = new PageImpl<>(List.of(proposal));
        Page<ProposalResponseDto> expectedPage = new PageImpl<>(List.of(proposalResponseDto));
        
        when(adSecurityUtil.checkAdOwnerPermissionAndReturn(1L, authentication)).thenReturn(null);
        when(proposalRepo.findAllByAdIdAndProposalStatus(1L, ProposalStatus.PENDING, pageable))
                .thenReturn(proposalPage);
        when(proposalMapper.toDto(proposalPage)).thenReturn(expectedPage);

        Page<ProposalResponseDto> result = proposalQueryService.findAllProposalsByAdId(
                1L, ProposalStatus.PENDING, pageable, authentication);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(adSecurityUtil).checkAdOwnerPermissionAndReturn(1L, authentication);
        verify(proposalRepo).findAllByAdIdAndProposalStatus(1L, ProposalStatus.PENDING, pageable);
    }

    @Test
    void FindByIdShouldReturnProposal() {
        when(proposalRepo.findById(1L)).thenReturn(Optional.of(proposal));

        Proposal result = proposalQueryService.findById(1L);

        assertNotNull(result);
        assertEquals(proposal.getId(), result.getId());
        verify(proposalRepo).findById(1L);
    }

    @Test
    void FindByIdShouldThrowExceptionWhenNotFound() {
        when(proposalRepo.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProposalNotFoundException.class, 
                () -> proposalQueryService.findById(1L));
        
        assertNotNull(exception);
        verify(proposalRepo).findById(1L);
    }

    @Test
    void FindDtoByIdShouldReturnProposalResponseDto() {
        when(proposalRepo.findById(1L)).thenReturn(Optional.of(proposal));
        when(proposalMapper.toDto(proposal)).thenReturn(proposalResponseDto);

        ProposalResponseDto result = proposalQueryService.findDtoById(1L);

        assertNotNull(result);
        assertEquals(proposalResponseDto.getId(), result.getId());
        verify(proposalRepo).findById(1L);
        verify(proposalMapper).toDto(proposal);
    }
}