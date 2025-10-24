package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.proposal.service.api.AuthorProposalCommandService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorProposalCommandServiceImpl implements AuthorProposalCommandService {
    private final ProposalRepository proposalRepo;
    private final ProposalMapper proposalMapper;

    @Override
    public ProposalResponseDto approve(long proposalId, Authentication authentication) {


    }

    @Override
    public ProposalResponseDto reject(long proposalId, Authentication authentication) {


    }
}
