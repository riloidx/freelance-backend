package org.matvey.freelancebackend.category.repository;

import org.matvey.freelancebackend.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameEn(String nameEn);
    Optional<Category> findByNameRu(String nameRu);
    boolean existsByNameEnOrNameRu(String nameEn, String nameRu);
}
