package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.ads.service.util.AdSecurityUtil;
import org.matvey.freelancebackend.ads.service.util.AdValidator;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.exception.ProposalNotFoundException;
import org.matvey.freelancebackend.proposal.mapper.ProposalMapper;
import org.matvey.freelancebackend.proposal.repository.ProposalRepository;
import org.matvey.freelancebackend.proposal.service.api.ProposalQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProposalQueryServiceImpl implements ProposalQueryService {
    private final ProposalRepository proposalRepo;
    private final ProposalMapper proposalMapper;
    private final AdSecurityUtil adSecurityUtil;

    @Override
    public Page<ProposalResponseDto> findAllProposalsByAdId(long adId,
                                                            ProposalStatus status,
                                                            Pageable pageable,
                                                            Authentication authentication) {

        adSecurityUtil.checkAdOwnerPermission(adId, authentication);

        Page<Proposal> proposals = proposalRepo.findAllByAdId(adId, pageable);

        return proposalMapper.toDto(proposals);
    }

    @Override
    public Proposal findById(long id) {

        return proposalRepo.findById(id).
                orElseThrow(() -> new ProposalNotFoundException("id", String.valueOf(id)));
    }
}
