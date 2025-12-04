package org.matvey.freelancebackend.proposal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.ads.service.util.AdSecurityUtil;
import org.matvey.freelancebackend.common.util.LocalizationUtil;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalQueryServiceImpl implements ProposalQueryService {
    private final ProposalRepository proposalRepo;
    private final ProposalMapper proposalMapper;
    private final AdSecurityUtil adSecurityUtil;
    private final LocalizationUtil localizationUtil;

    @Override
    public Page<ProposalResponseDto> findAllProposalsByAdId(long adId,
                                                            ProposalStatus status,
                                                            Pageable pageable,
                                                            Authentication authentication) {
        log.debug("Finding proposals for ad id: {} with status: {}", adId, status);
        try {
            adSecurityUtil.checkAdOwnerPermissionAndReturn(adId, authentication);

            Page<Proposal> proposals;
            if (status != null) {
                proposals = proposalRepo.findAllByAdIdAndProposalStatus(adId, status, pageable);
            } else {
                proposals = proposalRepo.findAllByAdId(adId, pageable);
            }
            log.info("Found {} proposals for ad id: {}", proposals.getTotalElements(), adId);
            return proposalMapper.toDto(proposals);
        } catch (Exception e) {
            log.error("Error finding proposals for ad id: {}", adId, e);
            throw e;
        }
    }

    @Override
    public Proposal findById(long id) {
        log.debug("Finding proposal by id: {}", id);
        try {
            Proposal proposal = proposalRepo.findById(id).
                    orElseThrow(() -> {
                        log.warn("Proposal not found with id: {}", id);
                        return new ProposalNotFoundException(localizationUtil.getMessage("error.proposal.not.found", "id", String.valueOf(id)));
                    });
            log.debug("Found proposal with id: {}", id);
            return proposal;
        } catch (ProposalNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding proposal by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public ProposalResponseDto findDtoById(long id) {
        return proposalMapper.toDto(findById(id));
    }
    
    @Override
    public Page<ProposalResponseDto> findAllByFreelancer(Pageable pageable, Authentication authentication) {
        org.matvey.freelancebackend.security.user.CustomUserDetails userDetails = 
            (org.matvey.freelancebackend.security.user.CustomUserDetails) authentication.getPrincipal();
        
        Long freelancerId = userDetails.user().getId();
        log.debug("Finding all proposals by freelancer id: {}", freelancerId);
        try {
            Page<Proposal> proposals = proposalRepo.findAllByFreelancerIdAndAdIsNotNull(freelancerId, pageable);
            log.info("Found {} proposals for freelancer id: {}", proposals.getTotalElements(), freelancerId);
            return proposalMapper.toDto(proposals);
        } catch (Exception e) {
            log.error("Error finding proposals for freelancer id: {}", freelancerId, e);
            throw e;
        }
    }
    
    @Override
    public Page<ProposalResponseDto> findBuyerProposalsByFreelancer(Pageable pageable, Authentication authentication) {
        org.matvey.freelancebackend.security.user.CustomUserDetails userDetails = 
            (org.matvey.freelancebackend.security.user.CustomUserDetails) authentication.getPrincipal();
        
        Long freelancerId = userDetails.user().getId();
        log.debug("Finding buyer proposals for freelancer id: {}", freelancerId);
        try {
            Page<Proposal> proposals = proposalRepo.findAllByFreelancerIdAndAdIsNull(freelancerId, pageable);
            log.info("Found {} buyer proposals for freelancer id: {}", proposals.getTotalElements(), freelancerId);
            return proposalMapper.toDto(proposals);
        } catch (Exception e) {
            log.error("Error finding buyer proposals for freelancer id: {}", freelancerId, e);
            throw e;
        }
    }
}
