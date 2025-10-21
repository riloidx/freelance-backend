package org.matvey.freelancebackend.proposal.service.api;

import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.springframework.security.core.Authentication;

public interface OwnerProposalCommandService {
    ProposalResponseDto create(ProposalCreateDto proposalCreateDto);

    ProposalResponseDto cancel(long proposalId, Authentication authentication);
}
