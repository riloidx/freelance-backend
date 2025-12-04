package org.matvey.freelancebackend.category.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.category.dto.request.CategoryCreateDto;
import org.matvey.freelancebackend.category.dto.request.CategoryUpdateDto;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.entity.Category;
import org.matvey.freelancebackend.category.exception.CategoryAlreadyExistsException;
import org.matvey.freelancebackend.category.mapper.CategoryMapper;
import org.matvey.freelancebackend.category.repository.CategoryRepository;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryCommandServiceImplTest {

    @Mock
    private CategoryRepository categoryRepo;
    
    @Mock
    private CategoryMapper categoryMapper;
    
    @Mock
    private CategoryQueryService categoryQueryService;
    
    @InjectMocks
    private CategoryCommandServiceImpl categoryCommandService;
    
    private Category category;
    private CategoryCreateDto categoryCreateDto;
    private CategoryUpdateDto categoryUpdateDto;
    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setNameEn("Test Category");
        category.setNameRu("Тестовая категория");
        
        categoryCreateDto = new CategoryCreateDto();
        categoryCreateDto.setNameEn("Test Category");
        categoryCreateDto.setNameRu("Тестовая категория");
        
        categoryUpdateDto = new CategoryUpdateDto();
        categoryUpdateDto.setId(1L);
        categoryUpdateDto.setNameEn("Updated Category");
        
        categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(1L);
        categoryResponseDto.setNameEn("Test Category");
        categoryResponseDto.setNameRu("Тестовая категория");
    }

    @Test
    void CreateShouldReturnCategoryResponseDto() {
        when(categoryRepo.findByNameEn("Test Category")).thenReturn(Optional.empty());
        when(categoryRepo.findByNameRu("Тестовая категория")).thenReturn(Optional.empty());
        when(categoryMapper.toEntity(categoryCreateDto)).thenReturn(category);
        when(categoryRepo.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryCommandService.create(categoryCreateDto);

        assertNotNull(result);
        assertEquals(categoryResponseDto.getId(), result.getId());
        verify(categoryRepo).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void CreateShouldThrowExceptionWhenCategoryExists() {
        when(categoryRepo.findByNameEn("Test Category")).thenReturn(Optional.of(category));

        Exception exception = assertThrows(CategoryAlreadyExistsException.class, 
                () -> categoryCommandService.create(categoryCreateDto));
        
        assertNotNull(exception);
        verify(categoryRepo, never()).save(any());
    }

    @Test
    void UpdateShouldReturnUpdatedCategoryResponseDto() {
        when(categoryQueryService.findCategoryById(1L)).thenReturn(category);
        when(categoryRepo.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryCommandService.update(1L, categoryUpdateDto);

        assertNotNull(result);
        verify(categoryMapper).updateEntityFromDto(categoryUpdateDto, category);
        verify(categoryRepo).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void DeleteShouldRemoveCategory() {
        categoryCommandService.delete(1L);

        verify(categoryRepo).deleteById(1L);
    }
}