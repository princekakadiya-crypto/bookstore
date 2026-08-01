package com.tss.bookstore.mapper;

import com.tss.bookstore.dto.CategoryRequestDto;
import com.tss.bookstore.dto.CategoryResponseDto;
import com.tss.bookstore.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDto dto);

    CategoryResponseDto toDto(Category category);
}
