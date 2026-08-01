package com.tss.bookstore.service;

import com.tss.bookstore.dto.ReviewRequestDto;
import com.tss.bookstore.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto addReview(ReviewRequestDto request);

    ReviewResponseDto getReviewById(Long id);

    List<ReviewResponseDto> getAllReviews();

    ReviewResponseDto updateReview(Long id,ReviewRequestDto request);

    void deleteReview(Long id);
}
