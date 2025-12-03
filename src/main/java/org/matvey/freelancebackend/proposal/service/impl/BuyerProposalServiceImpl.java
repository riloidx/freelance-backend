package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.proposal.dto.request.BuyerProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.proposal.service.api.BuyerProposalService;
import org.matvey.freelancebackend.security.user.CustomUserDetails;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.exception.InsufficientBalanceException;
import org.matvey.freelancebackend.users.service.api.UserQueryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuyerProposalServiceImpl implements BuyerProposalService {
    private final ProposalRepository proposalRepository;
    private final ProposalMapper proposalMapper;
    private final UserQueryService userQueryService;
    private final org.matvey.freelancebackend.common.util.LocalizationUtil localizationUtil;

    @Override
    @Transactional
    public ProposalResponseDto createBuyerProposal(BuyerProposalCreateDto dto, Authentication authentication) {
        User buyer = ((CustomUserDetails) authentication.getPrincipal()).user();
        User freelancer = userQueryService.findUserById(dto.getFreelancerId());

        if (buyer.getBalance().compareTo(dto.getPrice()) < 0) {
            throw new InsufficientBalanceException(localizationUtil.getMessage("error.insufficient.balance"));
        }

        Proposal proposal = new Proposal();
        proposal.setPrice(dto.getPrice());
        proposal.setMessage(dto.getMessage());
        proposal.setProposalStatus(ProposalStatus.PENDING);
        proposal.setFreelancer(freelancer);
        proposal.setBuyer(buyer);
        proposal.setAd(null);

        Proposal savedProposal = proposalRepository.save(proposal);
        return proposalMapper.toDto(savedProposal);
    }
}
