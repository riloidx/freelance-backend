package org.matvey.freelancebackend.proposal.repository;

import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.proposal.entity.ProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    Page<Proposal> findAllByAdIdAndProposalStatus(Long adId, ProposalStatus status, Pageable pageable);
    
    Page<Proposal> findAllByFreelancerId(Long freelancerId, Pageable pageable);
}

