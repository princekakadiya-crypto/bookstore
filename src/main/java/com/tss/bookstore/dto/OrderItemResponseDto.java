package com.tss.bookstore.dto;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Long orderItemId;

    private Long bookId;

    private String bookTitle;

    private Integer quantity;

    private Double price;
}
