package com.tss.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookResponseDto {
    private Long bookId;

    private String title;

    private Double price;

    private String ISBN;

    private Integer stock;

    private String authorNames;

    private String publisherName;

    private String categoryName;
}
