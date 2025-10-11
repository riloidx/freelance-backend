package org.matvey.freelancebackend.category.service.impl;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;
    private final CategoryQueryService categoryQueryService;


    @Override
    @Transactional
    public CategoryResponseDto create(CategoryCreateDto categoryCreateDto) {
        isCategoryExistsByName(categoryCreateDto.getName());
        Category category = categoryMapper.toEntity(categoryCreateDto);

        Category savedCategory = categoryRepo.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponseDto update(long pathId, CategoryUpdateDto categoryUpdateDto) {
        ValidationUtils.ensureSameId("Category", pathId, categoryUpdateDto.getId());

        Category category = categoryQueryService.findCategoryById(pathId);
        categoryMapper.updateEntityFromDto(categoryUpdateDto, category);

        Category savedCategory = categoryRepo.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void delete(long id) {
        categoryRepo.deleteById(id);
    }

    private void isCategoryExistsByName(String name) {
        if (categoryRepo.findByName(name).isPresent()) {
            throw new CategoryAlreadyExistsException("name", name);
        }
    }
}
