package com.tss.bookstore.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequestDto {
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be minimum 1")
    @Max(value = 5, message = "Rating must be maximum 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    @Size(
            min = 3,
            max = 500,
            message = "Comment must be between 3 and 500 characters"
    )
    private String comment;

    @NotNull(message = "User id is required")
    @Positive(message = "User id must be positive")
    private Long userId;

    @NotNull(message = "Book id is required")
    @Positive(message = "Book id must be positive")
    private Long bookId;
}
