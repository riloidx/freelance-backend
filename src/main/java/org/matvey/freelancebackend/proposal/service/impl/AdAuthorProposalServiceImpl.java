package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.entity.Ad;
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

@Service
@RequiredArgsConstructor
public class AdAuthorProposalServiceImpl implements AdAuthorProposalService {
    private final ProposalRepository proposalRepository;
    private final ProposalQueryService proposalQueryService;
    private final ProposalMapper proposalMapper;
    private final AdSecurityUtil adSecurityUtil;
    private final ContractRepository contractRepository;
    private final UserProfileService userProfileService;

    @Override
    @Transactional
    public ProposalResponseDto approve(long proposalId, Authentication auth) {
        Proposal proposal = getProposalWithPermissionCheck(proposalId, auth);
        Ad ad = proposal.getAd();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        rejectOtherProposals(ad, proposalId);
        approveProposal(proposal);
        createContract(proposal, userDetails.user());

        return proposalMapper.toDto(proposal);
    }

    @Override
    @Transactional
    public ProposalResponseDto reject(long proposalId, Authentication auth) {
        Proposal proposal = getProposalWithPermissionCheck(proposalId, auth);
        rejectProposal(proposal);

        return proposalMapper.toDto(proposal);
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
        List<Proposal> proposals = ad.getProposals();
        proposals.stream()
                .filter(p -> !p.getId().equals(approvedProposalId))
                .filter(p -> p.getProposalStatus() != ProposalStatus.REJECTED)
                .forEach(p -> p.setProposalStatus(ProposalStatus.REJECTED));

        proposalRepository.saveAll(proposals);
    }

    private void rejectProposal(Proposal proposal) {
        proposal.setProposalStatus(ProposalStatus.REJECTED);

        proposalRepository.save(proposal);
    }

    private void createContract(Proposal proposal, User buyer) {
        userProfileService.subtractBalance(buyer.getId(), proposal.getPrice());

        Contract contract = new Contract();
        contract.setProposal(proposal);
        contract.setFreelancer(proposal.getFreelancer());
        contract.setBuyer(buyer);
        contract.setPrice(proposal.getPrice());
        contract.setContractStatus(ContractStatus.IN_PROGRESS);
        contract.setCreatedAt(Instant.now());

        contractRepository.save(contract);
    }
}
