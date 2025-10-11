package org.matvey.freelancebackend.category.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.matvey.freelancebackend.category.dto.request.CategoryCreateDto;
import org.matvey.freelancebackend.category.dto.request.CategoryUpdateDto;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.entity.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    List<CategoryResponseDto> toDto(List<Category> categoryList);

    Category toEntity(CategoryCreateDto categoryCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CategoryUpdateDto dto, @MappingTarget Category category);
}
