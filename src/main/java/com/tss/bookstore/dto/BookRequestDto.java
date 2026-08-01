package com.tss.bookstore.dto;

import jakarta.annotation.security.DenyAll;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class BookRequestDto {
    @NotBlank(message = "Book title is required")
    private String title;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock cannot be negative")
    private Integer stock;

    @NotEmpty(message = "At least one author is required")
    private Set<Long> authorIds;

    @NotNull(message = "Publisher id is required")
    private Long publisherId;

    @NotNull(message = "Category id is required")
    private Long categoryId;
}
