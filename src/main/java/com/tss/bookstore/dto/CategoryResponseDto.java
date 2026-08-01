package com.tss.bookstore.dto;

import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long categoryId;
    private String name;
    private String description;
}
