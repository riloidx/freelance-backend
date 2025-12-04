package org.matvey.freelancebackend.category.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.exception.CategoryNotFoundException;
import org.matvey.freelancebackend.category.mapper.CategoryMapper;
import org.matvey.freelancebackend.category.repository.CategoryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepo;
    
    @Mock
    private CategoryMapper categoryMapper;
    
    @InjectMocks
    private CategoryQueryServiceImpl categoryQueryService;
    
    private Category category;
    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setNameEn("Test Category");
        category.setNameRu("Тестовая категория");
        
        categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(1L);
        categoryResponseDto.setNameEn("Test Category");
        categoryResponseDto.setNameRu("Тестовая категория");
    }

    @Test
    void FindAllCategoriesDtoShouldReturnListOfCategories() {
        List<Category> categories = List.of(category);
        List<CategoryResponseDto> expectedDtos = List.of(categoryResponseDto);
        
        when(categoryRepo.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(categories)).thenReturn(expectedDtos);

        List<CategoryResponseDto> result = categoryQueryService.findAllCategoriesDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepo).findAll();
        verify(categoryMapper).toDto(categories);
    }

    @Test
    void FindCategoryByIdShouldReturnCategory() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryQueryService.findCategoryById(1L);

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        verify(categoryRepo).findById(1L);
    }

    @Test
    void FindCategoryByIdShouldThrowExceptionWhenNotFound() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, 
                () -> categoryQueryService.findCategoryById(1L));
        
        verify(categoryRepo).findById(1L);
    }

    @Test
    void FindCategoryDtoByIdShouldReturnCategoryResponseDto() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryQueryService.findCategoryDtoById(1L);

        assertNotNull(result);
        assertEquals(categoryResponseDto.getId(), result.getId());
        verify(categoryRepo).findById(1L);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void FindCategoryDtoByNameShouldReturnCategoryResponseDto() {
        when(categoryRepo.findByNameEn("Test Category")).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryQueryService.findCategoryDtoByName("Test Category");

        assertNotNull(result);
        assertEquals(categoryResponseDto.getNameEn(), result.getNameEn());
        verify(categoryRepo).findByNameEn("Test Category");
        verify(categoryMapper).toDto(category);
    }

    @Test
    void FindCategoryDtoByNameShouldThrowExceptionWhenNotFound() {
        when(categoryRepo.findByNameEn("Test Category")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, 
                () -> categoryQueryService.findCategoryDtoByName("Test Category"));
        
        verify(categoryRepo).findByNameEn("Test Category");
    }
}