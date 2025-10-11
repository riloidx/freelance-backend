package org.matvey.freelancebackend.category.controller.admin;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.category.dto.request.CategoryCreateDto;
import org.matvey.freelancebackend.category.dto.request.CategoryUpdateDto;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.service.api.CategoryCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryCommandService categoryCommandService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategoryDto(CategoryCreateDto categoryCreateDto) {
        var category = categoryCommandService.create(categoryCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategoryDto(@PathVariable long id,
                                                                 CategoryUpdateDto categoryUpdateDto) {
        var updatedCategory = categoryCommandService.update(id, categoryUpdateDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> deleteCategoryDto(@PathVariable long id) {
        categoryCommandService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
