package org.matvey.freelancebackend.category.service.api;

import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.entity.Category;

import java.util.List;

public interface CategoryQueryService {
    List<CategoryResponseDto> findAllCategoriesDto();

    Category findCategoryById(long id);

    CategoryResponseDto findCategoryDtoById(long id);

    CategoryResponseDto findCategoryDtoByName(String name);

}
