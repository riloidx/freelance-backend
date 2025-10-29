package org.matvey.freelancebackend.proposal.controller;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.service.api.AdAuthorProposalService;
import org.matvey.freelancebackend.proposal.service.api.FreelancerProposalService;
import org.matvey.freelancebackend.proposal.service.api.ProposalQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proposals")
@RequiredArgsConstructor
public class ProposalController {
    private final FreelancerProposalService freelancerProposalService;
    private final AdAuthorProposalService adAuthorProposalService;
    private final ProposalQueryService proposalQueryService;

    @PostMapping
    public ResponseEntity<ProposalResponseDto> createProposal(
            @RequestBody ProposalCreateDto proposalCreateDto,
            Authentication authentication
    ) {
        ProposalResponseDto response = freelancerProposalService.create(proposalCreateDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{proposalId}/approve")
    public ResponseEntity<ProposalResponseDto> approveProposal(
            @PathVariable long proposalId,
            Authentication authentication
    ) {
        ProposalResponseDto response = adAuthorProposalService.approve(proposalId, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{proposalId}/reject")
    public ResponseEntity<ProposalResponseDto> rejectProposal(
            @PathVariable long proposalId,
            Authentication authentication
    ) {
        ProposalResponseDto response = adAuthorProposalService.reject(proposalId, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<Page<ProposalResponseDto>> getProposalsByAd(
            @PathVariable long adId,
            @RequestParam(required = false) ProposalStatus status,
            Pageable pageable,
            Authentication authentication
    ) {
        Page<ProposalResponseDto> page = proposalQueryService.findAllProposalsByAdId(adId, status, pageable, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/{proposalId}")
    public ResponseEntity<ProposalResponseDto> getProposalById(@PathVariable long proposalId) {
        ProposalResponseDto dto = proposalQueryService.findDtoById(proposalId);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
