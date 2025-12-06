package org.matvey.freelancebackend.contracts.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.users.entity.User;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entity representing a work contract between a client and freelancer.
 * 
 * Contracts are created when a client accepts a freelancer's proposal.
 * They define the agreed price, track the contract status through its lifecycle,
 * and can include delivery URLs when work is completed.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "contract_status")
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "delivery_url", length = 1000)
    private String deliveryUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;
}
