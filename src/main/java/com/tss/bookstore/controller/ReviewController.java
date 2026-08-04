package com.tss.bookstore.controller;

import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.ReviewRequestDto;
import com.tss.bookstore.dto.ReviewResponseDto;
import com.tss.bookstore.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("app/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> addReview(@Valid @RequestBody ReviewRequestDto request){
        return new ResponseEntity<>(
                reviewService.addReview(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long id){
        return ResponseEntity.ok(
                reviewService.getReviewById(id)
        );
    }

    @GetMapping
    public ResponseEntity<PageDto<ReviewResponseDto>> getAll(Pageable pageable){
        return ResponseEntity.ok(
                reviewService.getAllReviews(pageable)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> update(@PathVariable Long id,@Valid @RequestBody ReviewRequestDto request){
        return ResponseEntity.ok(
                reviewService.updateReview(id,request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
