package org.matvey.freelancebackend.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.exception.CategoryNotFoundException;
import org.matvey.freelancebackend.category.mapper.CategoryMapper;
import org.matvey.freelancebackend.category.repository.CategoryRepository;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {
    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto>  findAllCategoriesDto() {
        List<Category> categories = categoryRepo.findAll();

        return categoryMapper.toDto(categories);
    }

    @Override
    public Category findCategoryById(long id) {
        return categoryRepo.findById(id).
                orElseThrow(() -> new CategoryNotFoundException("id", String.valueOf(id)));
    }

    @Override
    public CategoryResponseDto findCategoryDtoById(long id) {
        Category category = findCategoryById(id);

        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto findCategoryDtoByName(String name) {
        Category category = findCategoryByNameEn(name);

        return categoryMapper.toDto(category);
    }


    private Category findCategoryByNameEn(String nameEn) {
        return categoryRepo.findByNameEn(nameEn).
                orElseThrow(() -> new CategoryNotFoundException("nameEn", nameEn));
    }
}
