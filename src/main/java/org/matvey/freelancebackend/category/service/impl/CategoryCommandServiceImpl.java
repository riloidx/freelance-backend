package org.matvey.freelancebackend.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.category.dto.request.CategoryCreateDto;
import org.matvey.freelancebackend.category.dto.request.CategoryUpdateDto;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.exception.CategoryAlreadyExistsException;
import org.matvey.freelancebackend.category.mapper.CategoryMapper;
import org.matvey.freelancebackend.category.repository.CategoryRepository;
import org.matvey.freelancebackend.category.service.api.CategoryCommandService;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.matvey.freelancebackend.common.util.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;
    private final CategoryQueryService categoryQueryService;

    @Override
    @Transactional
    public CategoryResponseDto create(CategoryCreateDto categoryCreateDto) {
        log.debug("Creating new category with nameEn: {}, nameRu: {}", categoryCreateDto.getNameEn(), categoryCreateDto.getNameRu());
        try {
            validateCategoryUniqueness(categoryCreateDto.getNameEn(), categoryCreateDto.getNameRu(), null);
            Category category = categoryMapper.toEntity(categoryCreateDto);
            Category savedCategory = categoryRepo.save(category);
            log.info("Successfully created category with id: {}", savedCategory.getId());
            return categoryMapper.toDto(savedCategory);
        } catch (Exception e) {
            log.error("Error creating category with nameEn: {}", categoryCreateDto.getNameEn(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CategoryResponseDto update(long pathId, CategoryUpdateDto categoryUpdateDto) {
        log.debug("Updating category with id: {}", pathId);
        try {
            ValidationUtils.ensureSameId("Category", pathId, categoryUpdateDto.getId());
            Category category = categoryQueryService.findCategoryById(pathId);
            validateCategoryUniqueness(categoryUpdateDto.getNameEn(), categoryUpdateDto.getNameRu(), pathId);
            categoryMapper.updateEntityFromDto(categoryUpdateDto, category);
            Category savedCategory = categoryRepo.save(category);
            log.info("Successfully updated category with id: {}", savedCategory.getId());
            return categoryMapper.toDto(savedCategory);
        } catch (Exception e) {
            log.error("Error updating category with id: {}", pathId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.debug("Deleting category with id: {}", id);
        try {
            categoryRepo.deleteById(id);
            log.info("Successfully deleted category with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting category with id: {}", id, e);
            throw e;
        }
    }

    private void validateCategoryUniqueness(String nameEn, String nameRu, Long excludeId) {
        log.debug("Validating category uniqueness for nameEn: {}, nameRu: {}", nameEn, nameRu);
        categoryRepo.findByNameEn(nameEn).ifPresent(existing -> {
            if (!existing.getId().equals(excludeId)) {
                log.warn("Category with nameEn: {} already exists", nameEn);
                throw new CategoryAlreadyExistsException("nameEn", nameEn);
            }
        });
        
        categoryRepo.findByNameRu(nameRu).ifPresent(existing -> {
            if (!existing.getId().equals(excludeId)) {
                log.warn("Category with nameRu: {} already exists", nameRu);
                throw new CategoryAlreadyExistsException("nameRu", nameRu);
            }
        });
    }
}
