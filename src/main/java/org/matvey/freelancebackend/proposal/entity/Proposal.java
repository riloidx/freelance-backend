package org.matvey.freelancebackend.proposal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.users.entity.User;

import java.math.BigDecimal;
import java.time.Instant;

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
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User freelancer;

    @OneToOne(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Contract contract;
}