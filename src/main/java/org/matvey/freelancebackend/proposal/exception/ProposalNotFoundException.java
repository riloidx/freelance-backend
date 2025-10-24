package org.matvey.freelancebackend.proposal.exception;

public class ProposalNotFoundException extends RuntimeException {
    public ProposalNotFoundException(String field, String value) {
        super("Proposal with field " + field + "=" + value + " not found");
    }
}
