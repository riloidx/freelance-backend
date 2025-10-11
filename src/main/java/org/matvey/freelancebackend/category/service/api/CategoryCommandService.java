package org.matvey.freelancebackend.category.service.api;

import org.matvey.freelancebackend.category.dto.request.CategoryCreateDto;
import org.matvey.freelancebackend.category.dto.request.CategoryUpdateDto;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;

public interface CategoryCommandService {
    CategoryResponseDto create(CategoryCreateDto categoryCreateDto);

    CategoryResponseDto update(long id, CategoryUpdateDto categoryUpdateDto);

    void delete(long id);
}
