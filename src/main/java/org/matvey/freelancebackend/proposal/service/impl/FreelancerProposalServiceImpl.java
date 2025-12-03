package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.service.api.AdQueryService;
import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.proposal.service.api.FreelancerProposalService;
import org.matvey.freelancebackend.security.service.impl.UserDetailsServiceImpl;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreelancerProposalServiceImpl implements FreelancerProposalService {
    private final ProposalRepository proposalRepo;
    private final ProposalMapper proposalMapper;
    private final AdQueryService adQueryService;
    private final UserDetailsServiceImpl userDetailsService;
    private final org.matvey.freelancebackend.proposal.service.api.ProposalQueryService proposalQueryService;
    private final org.matvey.freelancebackend.contracts.repository.ContractRepository contractRepository;
    private final org.matvey.freelancebackend.users.service.api.UserProfileService userProfileService;

    @Override
    @Transactional
    public ProposalResponseDto create(ProposalCreateDto proposalCreateDto, Authentication authentication) {
        Proposal proposal = prepareProposal(proposalCreateDto, authentication);

        Proposal savedProposal = proposalRepo.save(proposal);

        return proposalMapper.toDto(savedProposal);
    }

    public Proposal prepareProposal(ProposalCreateDto proposalCreateDto, Authentication authentication) {
        Proposal proposal = proposalMapper.toEntity(proposalCreateDto);
        Ad ad = adQueryService.findAdById(proposalCreateDto.getAdId());
        User freelancer = ((CustomUserDetails) authentication.getPrincipal()).user();

        proposal.setProposalStatus(ProposalStatus.PENDING);
        proposal.setAd(ad);
        proposal.setFreelancer(freelancer);

        return proposal;
    }

    @Override
    @Transactional
    public ProposalResponseDto acceptBuyerProposal(long proposalId, Authentication authentication) {
        Proposal proposal = proposalQueryService.findById(proposalId);
        User freelancer = ((CustomUserDetails) authentication.getPrincipal()).user();

        if (!proposal.getFreelancer().getId().equals(freelancer.getId())) {
            throw new RuntimeException("You are not authorized to accept this proposal");
        }

        if (proposal.getAd() != null) {
            throw new RuntimeException("This is not a buyer proposal");
        }

        if (proposal.getBuyer() == null) {
            throw new RuntimeException("Buyer not found in proposal");
        }

        userProfileService.subtractBalance(proposal.getBuyer().getId(), proposal.getPrice());

        proposal.setProposalStatus(ProposalStatus.ACCEPTED);
        proposalRepo.save(proposal);

        org.matvey.freelancebackend.contracts.entity.Contract contract = new org.matvey.freelancebackend.contracts.entity.Contract();
        contract.setProposal(proposal);
        contract.setFreelancer(freelancer);
        contract.setBuyer(proposal.getBuyer());
        contract.setPrice(proposal.getPrice());
        contract.setContractStatus(org.matvey.freelancebackend.contracts.entity.ContractStatus.IN_PROGRESS);
        contract.setCreatedAt(java.time.Instant.now());

        contractRepository.save(contract);

        return proposalMapper.toDto(proposal);
    }

    @Override
    @Transactional
    public ProposalResponseDto rejectBuyerProposal(long proposalId, Authentication authentication) {
        Proposal proposal = proposalQueryService.findById(proposalId);
        User freelancer = ((CustomUserDetails) authentication.getPrincipal()).user();

        if (!proposal.getFreelancer().getId().equals(freelancer.getId())) {
            throw new RuntimeException("You are not authorized to reject this proposal");
        }

        if (proposal.getAd() != null) {
            throw new RuntimeException("This is not a buyer proposal");
        }

        proposal.setProposalStatus(ProposalStatus.REJECTED);
        proposalRepo.save(proposal);

        return proposalMapper.toDto(proposal);
    }

}
