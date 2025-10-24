package org.matvey.freelancebackend.proposal.service.api;

import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.springframework.security.core.Authentication;

public interface AuthorProposalCommandService {
    ProposalResponseDto approve(long proposalId, Authentication authentication);

    ProposalResponseDto reject(long proposalId, Authentication authentication);
}
