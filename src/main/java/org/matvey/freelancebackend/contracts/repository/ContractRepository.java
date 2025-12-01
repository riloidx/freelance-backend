package org.matvey.freelancebackend.contracts.repository;

import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.contracts.entity.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    @Query("""
                SELECT c FROM Contract c
                JOIN c.proposal p
                JOIN p.ad a
                WHERE a.user.id = :userId
                AND c.contractStatus = :status
            """)
    Page<Contract> findContractsByUserIdAndProposalStatus(long userId, ContractStatus status, Pageable pageable);
}
