package org.matvey.freelancebackend.proposal.exception;

public class ProposalNotFoundException extends RuntimeException {
    public ProposalNotFoundException(String message) {
        super(message);
    }
}
