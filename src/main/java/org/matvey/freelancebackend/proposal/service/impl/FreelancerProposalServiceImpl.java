package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.service.api.AdQueryService;
import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.proposal.service.api.FreelancerProposalService;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreelancerProposalServiceImpl implements FreelancerProposalService {
    private final ProposalRepository proposalRepo;
    private final ProposalMapper proposalMapper;
    private final AdQueryService adQueryService;
    private final org.matvey.freelancebackend.proposal.service.api.ProposalQueryService proposalQueryService;
    private final org.matvey.freelancebackend.contracts.repository.ContractRepository contractRepository;
    private final org.matvey.freelancebackend.users.service.api.UserProfileService userProfileService;

    @Override
    @Transactional
    public ProposalResponseDto create(ProposalCreateDto proposalCreateDto, Authentication authentication) {
        log.debug("Creating freelancer proposal for ad id: {}", proposalCreateDto.getAdId());
        try {
            Proposal proposal = prepareProposal(proposalCreateDto, authentication);
            Proposal savedProposal = proposalRepo.save(proposal);
            log.info("Successfully created freelancer proposal with id: {} for ad id: {}",
                    savedProposal.getId(), proposalCreateDto.getAdId());
            return proposalMapper.toDto(savedProposal);
        } catch (Exception e) {
            log.error("Error creating freelancer proposal for ad id: {}", proposalCreateDto.getAdId(), e);
            throw e;
        }
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
        User freelancer = ((CustomUserDetails) authentication.getPrincipal()).user();
        log.debug("Freelancer {} accepting buyer proposal with id: {}", freelancer.getId(), proposalId);

        try {
            Proposal proposal = proposalQueryService.findById(proposalId);

            if (!proposal.getFreelancer().getId().equals(freelancer.getId())) {
                log.warn("Freelancer {} attempted to accept proposal {} but is not authorized",
                        freelancer.getId(), proposalId);
                throw new RuntimeException("You are not authorized to accept this proposal");
            }

            if (proposal.getAd() != null) {
                log.warn("Proposal {} is not a buyer proposal", proposalId);
                throw new RuntimeException("This is not a buyer proposal");
            }

            if (proposal.getBuyer() == null) {
                log.error("Buyer not found in proposal {}", proposalId);
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
            log.info("Successfully accepted buyer proposal {} and created contract", proposalId);
            return proposalMapper.toDto(proposal);
        } catch (Exception e) {
            log.error("Error accepting buyer proposal {}", proposalId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ProposalResponseDto rejectBuyerProposal(long proposalId, Authentication authentication) {
        User freelancer = ((CustomUserDetails) authentication.getPrincipal()).user();
        log.debug("Freelancer {} rejecting buyer proposal with id: {}", freelancer.getId(), proposalId);

        try {
            Proposal proposal = proposalQueryService.findById(proposalId);

            if (!proposal.getFreelancer().getId().equals(freelancer.getId())) {
                log.warn("Freelancer {} attempted to reject proposal {} but is not authorized",
                        freelancer.getId(), proposalId);
                throw new RuntimeException("You are not authorized to reject this proposal");
            }

            if (proposal.getAd() != null) {
                log.warn("Proposal {} is not a buyer proposal", proposalId);
                throw new RuntimeException("This is not a buyer proposal");
            }

            proposal.setProposalStatus(ProposalStatus.REJECTED);
            proposalRepo.save(proposal);
            log.info("Successfully rejected buyer proposal {}", proposalId);
            return proposalMapper.toDto(proposal);
        } catch (Exception e) {
            log.error("Error rejecting buyer proposal {}", proposalId, e);
            throw e;
        }
    }

}
