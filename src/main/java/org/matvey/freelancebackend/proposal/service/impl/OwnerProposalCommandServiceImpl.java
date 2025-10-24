package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.proposal.service.api.OwnerProposalCommandService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerProposalCommandServiceImpl implements OwnerProposalCommandService {
    private final ProposalRepository proposalRepo;
    private final ProposalMapper proposalMapper;

    @Override
    public ProposalResponseDto create(ProposalCreateDto proposalCreateDto) {
        return null;
    }

    @Override
    public ProposalResponseDto cancel(long proposalId, Authentication authentication) {
        return null;
    }
}
