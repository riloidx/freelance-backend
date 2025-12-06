package org.matvey.freelancebackend.proposal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.users.entity.User;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entity representing a freelancer's proposal for a job advertisement.
 * 
 * Proposals are submitted by freelancers in response to job ads. They include
 * the proposed price, a message explaining their approach, and track the proposal
 * status. Accepted proposals can be converted into contracts.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "proposals")
@Getter
@Setter
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "proposal_status", nullable = false)
    private ProposalStatus proposalStatus;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = true)
    private Ad ad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @OneToOne(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Contract contract;
}