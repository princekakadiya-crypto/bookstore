package com.tss.bookstore.controller;

import com.tss.bookstore.dto.CategoryRequestDto;
import com.tss.bookstore.dto.CategoryResponseDto;
import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> addCategory(
            @Valid @RequestBody CategoryRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.addCategory(requestDto));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequestDto requestDto) {

        return ResponseEntity.ok(categoryService.updateCategory(categoryId, requestDto));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable Long categoryId) {

        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @GetMapping
    public ResponseEntity<PageDto<CategoryResponseDto>> getCategories(Pageable pageable) {

        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
