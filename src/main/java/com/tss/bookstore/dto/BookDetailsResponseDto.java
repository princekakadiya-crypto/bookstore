package com.tss.bookstore.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDetailsResponseDto {
    private Long bookId;

    private String title;

    private Double price;

    private String ISBN;

    private Integer stock;

    private String authorNames;

    private String publisherName;

    private String categoryName;
}
