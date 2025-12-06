package org.matvey.freelancebackend.category.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.matvey.freelancebackend.ads.entity.Ad;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Entity representing a job category in the freelance platform.
 * 
 * Categories are used to classify job advertisements and help users
 * filter and find relevant work opportunities. Each category supports
 * multilingual names (English and Russian) and tracks the number of associated ads.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @NotNull
    @Size(max = 32)
    @Column(name = "name_ru", nullable = false)
    private String nameRu;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "ads_count")
    private Integer adsCount;

    @OneToMany
    @JoinColumn(name = "category_id")
    private Set<Ad> ads = new LinkedHashSet<>();

}