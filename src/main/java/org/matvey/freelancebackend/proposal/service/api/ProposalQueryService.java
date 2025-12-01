package org.matvey.freelancebackend.proposal.service.api;

import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ProposalQueryService {
    Page<ProposalResponseDto> findAllProposalsByAdId(long adId,
                                                     ProposalStatus status,
                                                     Pageable pageable,
                                                     Authentication authentication);

    Proposal findById(long id);

    ProposalResponseDto findDtoById(long id);
    
    Page<ProposalResponseDto> findAllByFreelancer(Pageable pageable, Authentication authentication);

}
