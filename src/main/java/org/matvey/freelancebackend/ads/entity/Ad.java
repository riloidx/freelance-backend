package org.matvey.freelancebackend.ads.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.proposal.entity.Proposal;
import org.matvey.freelancebackend.users.entity.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Entity representing a job advertisement in the freelance platform.
 * 
 * Ads are created by clients to describe work they need done. Each ad contains
 * multilingual content (English and Russian), budget information, category classification,
 * and can receive multiple proposals from freelancers.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "ads")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title_en", nullable = false)
    private String titleEn;

    @Column(name = "title_ru", nullable = false)
    private String titleRu;

    @Column(name = "description_en", nullable = false)
    private String descriptionEn;

    @Column(name = "description_ru", nullable = false)
    private String descriptionRu;

    @Column(name = "ad_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdType adType;

    @Column(name = "budget", precision = 10, scale = 2)
    private BigDecimal budget;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AdStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> proposals;
}