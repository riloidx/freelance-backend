package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.ads.entity.AdStatus;
import org.matvey.freelancebackend.ads.repository.AdRepository;
import org.matvey.freelancebackend.ads.service.util.AdSecurityUtil;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.repository.ContractRepository;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.proposal.service.api.AdAuthorProposalService;
import org.matvey.freelancebackend.proposal.service.api.ProposalQueryService;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.service.api.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdAuthorProposalServiceImpl implements AdAuthorProposalService {
    private final ProposalRepository proposalRepository;
    private final ProposalQueryService proposalQueryService;
    private final ProposalMapper proposalMapper;
    private final AdSecurityUtil adSecurityUtil;
    private final ContractRepository contractRepository;
    private final UserProfileService userProfileService;
    private final AdRepository adRepository;

    @Override
    @Transactional
    public ProposalResponseDto approve(long proposalId, Authentication auth) {
        log.debug("Approving proposal with id: {}", proposalId);
        try {
            Proposal proposal = getProposalWithPermissionCheck(proposalId, auth);
            Ad ad = proposal.getAd();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

            rejectOtherProposals(ad, proposalId);
            approveProposal(proposal);
            archiveAd(ad);
            createContract(proposal, userDetails.user());
            log.info("Successfully approved proposal with id: {} and created contract", proposalId);
            return proposalMapper.toDto(proposal);
        } catch (Exception e) {
            log.error("Error approving proposal with id: {}", proposalId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ProposalResponseDto reject(long proposalId, Authentication auth) {
        log.debug("Rejecting proposal with id: {}", proposalId);
        try {
            Proposal proposal = getProposalWithPermissionCheck(proposalId, auth);
            rejectProposal(proposal);
            log.info("Successfully rejected proposal with id: {}", proposalId);
            return proposalMapper.toDto(proposal);
        } catch (Exception e) {
            log.error("Error rejecting proposal with id: {}", proposalId, e);
            throw e;
        }
    }

    private Proposal getProposalWithPermissionCheck(long proposalId, Authentication auth) {
        Proposal proposal = proposalQueryService.findById(proposalId);
        long adId = proposal.getAd().getId();

        adSecurityUtil.checkAdOwnerPermissionAndReturn(adId, auth);

        return proposal;
    }

    private void approveProposal(Proposal proposal) {
        proposal.setProposalStatus(ProposalStatus.ACCEPTED);
        proposalRepository.save(proposal);
    }

    private void rejectOtherProposals(Ad ad, long approvedProposalId) {
        log.debug("Rejecting other proposals for ad id: {}, except proposal id: {}", ad.getId(), approvedProposalId);
        List<Proposal> proposals = ad.getProposals();
        long rejectedCount = proposals.stream()
                .filter(p -> !p.getId().equals(approvedProposalId))
                .filter(p -> p.getProposalStatus() != ProposalStatus.REJECTED)
                .peek(p -> p.setProposalStatus(ProposalStatus.REJECTED))
                .count();

        proposalRepository.saveAll(proposals);
        log.debug("Rejected {} other proposals for ad id: {}", rejectedCount, ad.getId());
    }

    private void rejectProposal(Proposal proposal) {
        proposal.setProposalStatus(ProposalStatus.REJECTED);

        proposalRepository.save(proposal);
    }

    private void createContract(Proposal proposal, User buyer) {
        log.debug("Creating contract for proposal id: {}, buyer: {}, freelancer: {}", 
                  proposal.getId(), buyer.getId(), proposal.getFreelancer().getId());
        userProfileService.subtractBalance(buyer.getId(), proposal.getPrice());

        Contract contract = new Contract();
        contract.setProposal(proposal);
        contract.setFreelancer(proposal.getFreelancer());
        contract.setBuyer(buyer);
        contract.setPrice(proposal.getPrice());
        contract.setContractStatus(ContractStatus.IN_PROGRESS);
        contract.setCreatedAt(Instant.now());

        contractRepository.save(contract);
        log.debug("Contract created successfully for proposal id: {}", proposal.getId());
    }

    private void archiveAd(Ad ad) {
        ad.setStatus(AdStatus.ARCHIVED);
        adRepository.save(ad);
    }
}
