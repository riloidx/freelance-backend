package org.matvey.freelancebackend.proposal.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.service.api.AdQueryService;
import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreelancerProposalServiceImplTest {

    @Mock
    private ProposalRepository proposalRepo;

    @Mock
    private ProposalMapper proposalMapper;

    @Mock
    private AdQueryService adQueryService;

    @Mock
    private org.matvey.freelancebackend.proposal.service.api.ProposalQueryService proposalQueryService;

    @Mock
    private org.matvey.freelancebackend.contracts.repository.ContractRepository contractRepository;

    @Mock
    private org.matvey.freelancebackend.users.service.api.UserProfileService userProfileService;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails userDetails;

    @InjectMocks
    private FreelancerProposalServiceImpl freelancerProposalService;

    private Proposal proposal;
    private ProposalCreateDto proposalCreateDto;
    private ProposalResponseDto proposalResponseDto;
    private Ad ad;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        ad = new Ad();
        ad.setId(1L);

        proposal = new Proposal();
        proposal.setId(1L);
        proposal.setMessage("Test proposal");
        proposal.setPrice(BigDecimal.valueOf(500));
        proposal.setProposalStatus(ProposalStatus.PENDING);
        proposal.setAd(ad);
        proposal.setFreelancer(user);

        proposalCreateDto = new ProposalCreateDto();
        proposalCreateDto.setAdId(1L);
        proposalCreateDto.setMessage("Test proposal");
        proposalCreateDto.setPrice(BigDecimal.valueOf(500));

        proposalResponseDto = new ProposalResponseDto();
        proposalResponseDto.setId(1L);
        proposalResponseDto.setMessage("Test proposal");
        proposalResponseDto.setProposalStatus(ProposalStatus.PENDING.name());
    }

    @Test
    void CreateShouldReturnProposalResponseDto() {
        when(proposalMapper.toEntity(proposalCreateDto)).thenReturn(proposal);
        when(adQueryService.findAdById(1L)).thenReturn(ad);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(proposalRepo.save(any(Proposal.class))).thenReturn(proposal);
        when(proposalMapper.toDto(proposal)).thenReturn(proposalResponseDto);

        ProposalResponseDto result = freelancerProposalService.create(proposalCreateDto, authentication);

        assertNotNull(result);
        assertEquals(proposalResponseDto.getId(), result.getId());
        verify(proposalRepo).save(any(Proposal.class));
        verify(proposalMapper).toDto(proposal);
    }

    @Test
    void PrepareProposalShouldSetCorrectFields() {
        when(proposalMapper.toEntity(proposalCreateDto)).thenReturn(proposal);
        when(adQueryService.findAdById(1L)).thenReturn(ad);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);

        Proposal result = freelancerProposalService.prepareProposal(proposalCreateDto, authentication);

        assertNotNull(result);
        assertEquals(ProposalStatus.PENDING, result.getProposalStatus());
        assertEquals(ad, result.getAd());
        assertEquals(user, result.getFreelancer());
    }

    @Test
    void AcceptBuyerProposalShouldCreateContract() {
        User buyer = new User();
        buyer.setId(2L);
        buyer.setBalance(BigDecimal.valueOf(1000));

        proposal.setAd(null);
        proposal.setBuyer(buyer);
        proposal.setFreelancer(user);

        when(proposalQueryService.findById(1L)).thenReturn(proposal);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(proposalRepo.save(any(Proposal.class))).thenReturn(proposal);
        when(contractRepository.save(any(org.matvey.freelancebackend.contracts.entity.Contract.class)))
                .thenReturn(new org.matvey.freelancebackend.contracts.entity.Contract());
        when(proposalMapper.toDto(proposal)).thenReturn(proposalResponseDto);

        ProposalResponseDto result = freelancerProposalService.acceptBuyerProposal(1L, authentication);

        assertNotNull(result);
        verify(userProfileService).subtractBalance(buyer.getId(), proposal.getPrice());
        verify(contractRepository).save(any(org.matvey.freelancebackend.contracts.entity.Contract.class));
    }

    @Test
    void RejectBuyerProposalShouldRejectProposal() {
        proposal.setAd(null);
        proposal.setBuyer(new User());
        proposal.setFreelancer(user);

        when(proposalQueryService.findById(1L)).thenReturn(proposal);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(proposalRepo.save(any(Proposal.class))).thenReturn(proposal);
        when(proposalMapper.toDto(proposal)).thenReturn(proposalResponseDto);

        ProposalResponseDto result = freelancerProposalService.rejectBuyerProposal(1L, authentication);

        assertNotNull(result);
        assertEquals(ProposalStatus.REJECTED, proposal.getProposalStatus());
        verify(proposalRepo).save(proposal);
    }
}