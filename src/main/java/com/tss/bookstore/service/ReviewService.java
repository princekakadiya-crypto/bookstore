package com.tss.bookstore.service;

import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.ReviewRequestDto;
import com.tss.bookstore.dto.ReviewResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto addReview(ReviewRequestDto request);

    ReviewResponseDto getReviewById(Long id);

    PageDto<ReviewResponseDto> getAllReviews(Pageable pageable);

    ReviewResponseDto updateReview(Long id,ReviewRequestDto request);

    void deleteReview(Long id);
}
