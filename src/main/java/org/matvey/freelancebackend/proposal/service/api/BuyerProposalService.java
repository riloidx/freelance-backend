package org.matvey.freelancebackend.proposal.service.api;

import org.matvey.freelancebackend.proposal.dto.request.BuyerProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.springframework.security.core.Authentication;

public interface BuyerProposalService {
    ProposalResponseDto createBuyerProposal(BuyerProposalCreateDto dto, Authentication authentication);
}
