package org.matvey.freelancebackend.contracts.exception;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(String field, String value) {
        super("Contract with " + field + "=" + value + " not found");
    }
}