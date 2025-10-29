package org.matvey.freelancebackend.proposal.service.api;

import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.springframework.security.core.Authentication;

public interface FreelancerProposalService {
    ProposalResponseDto create(ProposalCreateDto proposalCreateDto, Authentication authentication);
}
