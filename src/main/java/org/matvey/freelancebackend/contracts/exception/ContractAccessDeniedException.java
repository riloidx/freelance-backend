package org.matvey.freelancebackend.contracts.exception;

public class ContractAccessDeniedException extends RuntimeException {
    public ContractAccessDeniedException(long contractId, long userId) {
        super("User with id=" + userId + " has no access to contract with id=" + contractId);
    }
}