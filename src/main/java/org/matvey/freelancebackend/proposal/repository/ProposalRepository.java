package org.matvey.freelancebackend.proposal.repository;

import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
