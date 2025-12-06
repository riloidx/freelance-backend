package org.matvey.freelancebackend.reviews.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.matvey.freelancebackend.ads.entity.Ad;
import org.matvey.freelancebackend.users.entity.User;

import java.time.Instant;

/**
 * Entity representing a review in the freelance platform.
 * 
 * Reviews allow users to rate and provide feedback about their experience
 * working with other users. They are associated with specific job advertisements
 * and include both a numerical rating and descriptive text.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
}
