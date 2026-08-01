package com.tss.bookstore.service;

import com.tss.bookstore.dto.CategoryRequestDto;
import com.tss.bookstore.dto.CategoryResponseDto;
import com.tss.bookstore.dto.PageDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponseDto addCategory(CategoryRequestDto dto);

    CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto dto);

    CategoryResponseDto getCategoryById(Long categoryId);

    PageDto<CategoryResponseDto> getAllCategories(Pageable pageable);

    void deleteCategory(Long categoryId);
}
