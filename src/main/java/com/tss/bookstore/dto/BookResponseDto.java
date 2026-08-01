package com.tss.bookstore.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class BookResponseDto {
    private Long bookId;

    private String title;

    private Double price;

    private Integer stock;

    private Set<Long> authorIds;

    private Long publisherId;

    private Long categoryId;
}
