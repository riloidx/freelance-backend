package org.matvey.freelancebackend.contracts.controller.user;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.contracts.dto.response.ContractResponseDto;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.matvey.freelancebackend.contracts.service.api.ContractCommandService;
import org.matvey.freelancebackend.contracts.service.api.ContractQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {
    private final ContractCommandService contractCommandService;
    private final ContractQueryService contractQueryService;

    @GetMapping
    public ResponseEntity<Page<ContractResponseDto>> findMyContracts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(required = false) ContractStatus status,
            Authentication authentication) {
        
        Page<ContractResponseDto> contracts = contractQueryService.findMyContracts(
                pageable, status, authentication);
        
        return ResponseEntity.status(HttpStatus.OK).body(contracts);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ContractResponseDto> completeWork(
            @PathVariable long id,
            @RequestBody org.matvey.freelancebackend.contracts.dto.request.CompleteWorkDto dto,
            Authentication authentication) {
        
        ContractResponseDto contract = contractCommandService.completeWork(id, dto.getDeliveryUrl(), authentication);
        
        return ResponseEntity.status(HttpStatus.OK).body(contract);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ContractResponseDto> acceptWork(
            @PathVariable long id,
            Authentication authentication) {
        
        ContractResponseDto contract = contractCommandService.acceptWork(id, authentication);
        
        return ResponseEntity.status(HttpStatus.OK).body(contract);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ContractResponseDto> rejectWork(
            @PathVariable long id,
            Authentication authentication) {
        
        ContractResponseDto contract = contractCommandService.rejectWork(id, authentication);
        
        return ResponseEntity.status(HttpStatus.OK).body(contract);
    }
}