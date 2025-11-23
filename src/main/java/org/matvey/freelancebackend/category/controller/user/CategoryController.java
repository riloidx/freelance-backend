package org.matvey.freelancebackend.category.controller.user;

import lombok.RequiredArgsConstructor;
import org.matvey.freelancebackend.category.dto.response.CategoryResponseDto;
import org.matvey.freelancebackend.category.service.api.CategoryQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryQueryService categoryQueryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAllCategoriesDto() {
        var categories = categoryQueryService.findAllCategoriesDto();

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findCategoryDtoById(@PathVariable long id) {
        var category = categoryQueryService.findCategoryDtoById(id);

        return ResponseEntity.status(HttpStatus.OK).body(category);
    }
}
