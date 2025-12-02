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
                WHERE (c.freelancer.id = :userId OR c.buyer.id = :userId)
                AND (:status IS NULL OR c.contractStatus = :status)
            """)
    Page<Contract> findContractsByUserIdAndStatus(long userId, ContractStatus status, Pageable pageable);
}
