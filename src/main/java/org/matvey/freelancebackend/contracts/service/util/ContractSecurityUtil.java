package org.matvey.freelancebackend.contracts.service.util;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.service.api.ContractQueryService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractSecurityUtil {
    private final ContractQueryService contractQueryService;

    public Contract getContractIfOwner(long contractId, Authentication auth) {
        Contract contract = contractQueryService.findById(contractId);

        if (auth == null || !auth.getName().equals(contract.getProposal().getAd().getUser().getEmail())) {
            throw new AccessDeniedException("You are not allowed to this contract");
        }

        return contract;
    }
}
