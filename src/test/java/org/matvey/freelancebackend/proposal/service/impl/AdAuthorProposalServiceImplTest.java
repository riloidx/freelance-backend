package org.matvey.freelancebackend.proposal.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.service.util.AdSecurityUtil;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.proposal.service.api.ProposalQueryService;
import org.matvey.freelancebackend.users.entity.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdAuthorProposalServiceImplTest {

    @Mock
    private ProposalRepository proposalRepository;
    
    @Mock
    private ProposalQueryService proposalQueryService;
    
    @Mock
    private ProposalMapper proposalMapper;
    
    @Mock
    private AdSecurityUtil adSecurityUtil;
    
    @Mock
    private org.matvey.freelancebackend.contracts.repository.ContractRepository contractRepository;
    
    @Mock
    private org.matvey.freelancebackend.users.service.api.UserProfileService userProfileService;
    
    @Mock
    private org.matvey.freelancebackend.ads.repository.AdRepository adRepository;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private org.matvey.freelancebackend.security.user.CustomUserDetails userDetails;
    
    @InjectMocks
    private AdAuthorProposalServiceImpl adAuthorProposalService;
    
    private Proposal proposal;
    private Proposal otherProposal;
    private Ad ad;
    private ProposalResponseDto proposalResponseDto;
    private User buyer;
    private User freelancer;

    @BeforeEach
    void setUp() {
        buyer = new org.matvey.freelancebackend.users.entity.User();
        buyer.setId(1L);
        buyer.setBalance(java.math.BigDecimal.valueOf(1000));
        
        freelancer = new org.matvey.freelancebackend.users.entity.User();
        freelancer.setId(2L);
        
        ad = new Ad();
        ad.setId(1L);
        ad.setStatus(org.matvey.freelancebackend.ads.entity.AdStatus.ACTIVE);
        
        proposal = new Proposal();
        proposal.setId(1L);
        proposal.setProposalStatus(ProposalStatus.PENDING);
        proposal.setAd(ad);
        proposal.setFreelancer(freelancer);
        proposal.setPrice(java.math.BigDecimal.valueOf(100));
        
        otherProposal = new Proposal();
        otherProposal.setId(2L);
        otherProposal.setProposalStatus(ProposalStatus.PENDING);
        
        List<Proposal> proposals = new ArrayList<>();
        proposals.add(proposal);
        proposals.add(otherProposal);
        ad.setProposals(proposals);
        
        proposalResponseDto = new ProposalResponseDto();
        proposalResponseDto.setId(1L);
        proposalResponseDto.setProposalStatus(ProposalStatus.ACCEPTED.name());
    }

    @Test
    void ApproveShouldAcceptProposalAndRejectOthers() {
        when(proposalQueryService.findById(1L)).thenReturn(proposal);
        when(adSecurityUtil.checkAdOwnerPermissionAndReturn(1L, authentication)).thenReturn(ad);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(buyer);
        when(proposalRepository.save(proposal)).thenReturn(proposal);
        when(adRepository.save(ad)).thenReturn(ad);
        when(contractRepository.save(any(org.matvey.freelancebackend.contracts.entity.Contract.class)))
                .thenReturn(new org.matvey.freelancebackend.contracts.entity.Contract());
        when(proposalMapper.toDto(proposal)).thenReturn(proposalResponseDto);

        ProposalResponseDto result = adAuthorProposalService.approve(1L, authentication);

        assertNotNull(result);
        assertEquals(ProposalStatus.ACCEPTED, proposal.getProposalStatus());
        assertEquals(ProposalStatus.REJECTED, otherProposal.getProposalStatus());
        assertEquals(org.matvey.freelancebackend.ads.entity.AdStatus.ARCHIVED, ad.getStatus());
        verify(proposalRepository).save(proposal);
        verify(proposalRepository).saveAll(any(List.class));
        verify(contractRepository).save(any(org.matvey.freelancebackend.contracts.entity.Contract.class));
        verify(userProfileService).subtractBalance(buyer.getId(), proposal.getPrice());
        verify(adRepository).save(ad);
    }

    @Test
    void RejectShouldRejectProposal() {
        when(proposalQueryService.findById(1L)).thenReturn(proposal);
        when(adSecurityUtil.checkAdOwnerPermissionAndReturn(1L, authentication)).thenReturn(ad);
        when(proposalRepository.save(proposal)).thenReturn(proposal);
        when(proposalMapper.toDto(proposal)).thenReturn(proposalResponseDto);

        ProposalResponseDto result = adAuthorProposalService.reject(1L, authentication);

        assertNotNull(result);
        assertEquals(ProposalStatus.REJECTED, proposal.getProposalStatus());
        verify(proposalRepository).save(proposal);
    }
}