package org.matvey.freelancebackend.users.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.contracts.entity.Contract;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.reviews.entity.Review;
import org.matvey.freelancebackend.roles.entity.Role;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "description")
    private String description;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Ad> ads = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Review> receivedReviews = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Review> writtenReviews = new ArrayList<>();

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Proposal> proposals = new ArrayList<>();

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}