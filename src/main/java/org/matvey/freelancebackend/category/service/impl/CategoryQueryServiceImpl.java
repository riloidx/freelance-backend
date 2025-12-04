package org.matvey.freelancebackend.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.exception.CategoryNotFoundException;
import org.matvey.freelancebackend.category.mapper.CategoryMapper;
import org.matvey.freelancebackend.category.repository.CategoryRepository;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {
    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto>  findAllCategoriesDto() {
        log.debug("Finding all categories");
        try {
            List<Category> categories = categoryRepo.findAll();
            log.info("Found {} categories", categories.size());
            return categoryMapper.toDto(categories);
        } catch (Exception e) {
            log.error("Error finding all categories", e);
            throw e;
        }
    }

    @Override
    public Category findCategoryById(long id) {
        log.debug("Finding category by id: {}", id);
        try {
            Category category = categoryRepo.findById(id).
                    orElseThrow(() -> {
                        log.warn("Category not found with id: {}", id);
                        return new CategoryNotFoundException("id", String.valueOf(id));
                    });
            log.debug("Found category with id: {}", id);
            return category;
        } catch (CategoryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding category by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public CategoryResponseDto findCategoryDtoById(long id) {
        log.debug("Finding category DTO by id: {}", id);
        Category category = findCategoryById(id);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto findCategoryDtoByName(String name) {
        log.debug("Finding category DTO by name: {}", name);
        Category category = findCategoryByNameEn(name);
        return categoryMapper.toDto(category);
    }


    private Category findCategoryByNameEn(String nameEn) {
        log.debug("Finding category by nameEn: {}", nameEn);
        try {
            Category category = categoryRepo.findByNameEn(nameEn).
                    orElseThrow(() -> {
                        log.warn("Category not found with nameEn: {}", nameEn);
                        return new CategoryNotFoundException("nameEn", nameEn);
                    });
            log.debug("Found category with nameEn: {}", nameEn);
            return category;
        } catch (CategoryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding category by nameEn: {}", nameEn, e);
            throw e;
        }
    }
}
