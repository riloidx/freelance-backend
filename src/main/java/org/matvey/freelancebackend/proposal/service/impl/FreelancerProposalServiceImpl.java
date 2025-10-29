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
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreelancerProposalServiceImpl implements FreelancerProposalService {
    private final ProposalRepository proposalRepo;
    private final ProposalMapper proposalMapper;
    private final AdQueryService adQueryService;
    private final UserQueryService userQueryService;

    @Override
    public ProposalResponseDto create(ProposalCreateDto proposalCreateDto, Authentication authentication) {
        Proposal proposal = prepareProposal(proposalCreateDto, authentication);

        Proposal savedProposal = proposalRepo.save(proposal);

        return proposalMapper.toDto(savedProposal);
    }

    public Proposal prepareProposal(ProposalCreateDto proposalCreateDto, Authentication authentication) {
        Proposal proposal = proposalMapper.toEntity(proposalCreateDto);
        Ad ad = adQueryService.findAdById(proposalCreateDto.getAdId());
        User freelancer = userQueryService.findUserByEmail(authentication.getName());

        proposal.setProposalStatus(ProposalStatus.PENDING);
        proposal.setAd(ad);
        proposal.setFreelancer(freelancer);

        return proposal;
    }

}
