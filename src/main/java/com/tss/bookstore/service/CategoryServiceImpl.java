package com.tss.bookstore.service;

import com.tss.bookstore.dto.CategoryRequestDto;
import com.tss.bookstore.dto.CategoryResponseDto;
import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.entity.Category;
import com.tss.bookstore.exception.DuplicateResourceException;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.mapper.CategoryMapper;
import com.tss.bookstore.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDto addCategory(CategoryRequestDto requestDto) {

        if (categoryRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new DuplicateResourceException("Category already exists with name : " + requestDto.getName());
        }

        Category category = categoryMapper.toEntity(requestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto) {

        Category category = categoryRepository.findByCategoryIdAndIsActiveTrue(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id : " + categoryId));

        if (categoryRepository.existsByNameIgnoreCaseAndCategoryIdNot(
                requestDto.getName(), categoryId)) {

            throw new DuplicateResourceException("Category already exists with name : " + requestDto.getName());
        }

        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());

        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {

        Category category = categoryRepository.findByCategoryIdAndIsActiveTrue(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id : " + categoryId));

        return categoryMapper.toDto(category);
    }

    @Override
    public PageDto<CategoryResponseDto> getAllCategories(Pageable pageable) {

        Page<Category> categories = categoryRepository.findByIsActiveTrue(pageable);

        List<CategoryResponseDto> responseDtos = new ArrayList<>();

        for (Category category : categories.getContent()) {
            CategoryResponseDto dto = categoryMapper.toDto(category);
            responseDtos.add(dto);
        }

        PageDto<CategoryResponseDto> pageDto = new PageDto<>();

        pageDto.setContent(responseDtos);
        pageDto.setCurrentPage(categories.getNumber());
        pageDto.setPageSize(categories.getSize());
        pageDto.setTotalPages(categories.getTotalPages());
        pageDto.setTotalElements(categories.getTotalElements());
        pageDto.setFirst(categories.isFirst());
        pageDto.setLast(categories.isLast());
        pageDto.setEmpty(categories.isEmpty());

        return pageDto;
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findByCategoryIdAndIsActiveTrue(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id : " + categoryId));

        category.setIsActive(false);
    }
}
