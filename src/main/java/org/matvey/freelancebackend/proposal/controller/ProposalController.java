package org.matvey.freelancebackend.proposal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.proposal.dto.request.BuyerProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.request.ProposalCreateDto;
import org.matvey.freelancebackend.proposal.dto.response.ProposalResponseDto;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.matvey.freelancebackend.proposal.service.api.AdAuthorProposalService;
import org.matvey.freelancebackend.proposal.service.api.BuyerProposalService;
import org.matvey.freelancebackend.proposal.service.api.FreelancerProposalService;
import org.matvey.freelancebackend.proposal.service.api.ProposalQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/proposals")
@RequiredArgsConstructor
public class ProposalController {
    private final FreelancerProposalService freelancerProposalService;
    private final AdAuthorProposalService adAuthorProposalService;
    private final ProposalQueryService proposalQueryService;
    private final BuyerProposalService buyerProposalService;

    @PostMapping
    public ResponseEntity<ProposalResponseDto> createProposal(
            @RequestBody ProposalCreateDto proposalCreateDto,
            Authentication authentication
    ) {
        log.info("POST /proposals - Creating new proposal");
        ProposalResponseDto response = freelancerProposalService.create(proposalCreateDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{proposalId}/approve")
    public ResponseEntity<ProposalResponseDto> approveProposal(
            @PathVariable long proposalId,
            Authentication authentication
    ) {
        log.info("POST /proposals/{}/approve - Approving proposal", proposalId);
        ProposalResponseDto response = adAuthorProposalService.approve(proposalId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{proposalId}/reject")
    public ResponseEntity<ProposalResponseDto> rejectProposal(
            @PathVariable long proposalId,
            Authentication authentication
    ) {
        log.info("POST /proposals/{}/reject - Rejecting proposal", proposalId);
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
    
    @GetMapping("/me")
    public ResponseEntity<Page<ProposalResponseDto>> getMyProposals(
            Pageable pageable,
            Authentication authentication
    ) {
        Page<ProposalResponseDto> page = proposalQueryService.findAllByFreelancer(pageable, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
    
    @PostMapping("/buyer")
    public ResponseEntity<ProposalResponseDto> createBuyerProposal(
            @RequestBody BuyerProposalCreateDto dto,
            Authentication authentication
    ) {
        ProposalResponseDto response = buyerProposalService.createBuyerProposal(dto, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/buyer/received")
    public ResponseEntity<Page<ProposalResponseDto>> getBuyerProposals(
            Pageable pageable,
            Authentication authentication
    ) {
        Page<ProposalResponseDto> page = proposalQueryService.findBuyerProposalsByFreelancer(pageable, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
    
    @PostMapping("/buyer/{proposalId}/accept")
    public ResponseEntity<ProposalResponseDto> acceptBuyerProposal(
            @PathVariable long proposalId,
            Authentication authentication
    ) {
        ProposalResponseDto response = freelancerProposalService.acceptBuyerProposal(proposalId, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PostMapping("/buyer/{proposalId}/reject")
    public ResponseEntity<ProposalResponseDto> rejectBuyerProposal(
            @PathVariable long proposalId,
            Authentication authentication
    ) {
        ProposalResponseDto response = freelancerProposalService.rejectBuyerProposal(proposalId, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
