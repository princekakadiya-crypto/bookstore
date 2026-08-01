package com.tss.bookstore.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewResponseDto {
    private Long reviewId;

    private Integer rating;

    private String comment;

    private LocalDate reviewDate;

    private Long userId;

    private Long bookId;
}
