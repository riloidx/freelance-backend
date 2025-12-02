package org.matvey.freelancebackend.contracts.exception;

public class ContractAccessDeniedException extends RuntimeException {
    public ContractAccessDeniedException(String msg) {
        super(msg);
    }
}