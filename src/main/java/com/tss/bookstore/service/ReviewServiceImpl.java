package com.tss.bookstore.service;

import com.tss.bookstore.dto.ReviewRequestDto;
import com.tss.bookstore.dto.ReviewResponseDto;
import com.tss.bookstore.entity.Book;
import com.tss.bookstore.entity.Review;
import com.tss.bookstore.entity.User;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.repository.BookRepository;
import com.tss.bookstore.repository.ReviewRepository;
import com.tss.bookstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public ReviewResponseDto addReview(ReviewRequestDto request){
        Review review=new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setReviewDate(LocalDate.now());

        User user = userRepository.findByUserIdAndIsActiveTrue(request.getUserId()).orElseThrow(
                ()->new NotFoundException("User not found"));

        Book book = bookRepository.findByBookIdAndIsActiveTrue(request.getBookId()).orElseThrow(
                                ()->new NotFoundException("Book not found"));
        review.setUser(user);
        review.setBook(book);

        return mapToDto(
                reviewRepository.save(review)
        );
    }

    @Override
    public ReviewResponseDto getReviewById(Long id){

        Review review = reviewRepository.findByReviewIdAndIsActiveTrue(id).orElseThrow(
                ()->new NotFoundException("Review not found"));
        return mapToDto(review);

    }

    @Override
    public List<ReviewResponseDto> getAllReviews(){
        return reviewRepository.findAll().stream().filter(Review::getIsActive)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDto updateReview(Long id, ReviewRequestDto request){
        Review review = reviewRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Review not found"));
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return mapToDto(
                reviewRepository.save(review)
        );

    }

    @Override
    public void deleteReview(Long id){
        Review review = reviewRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Review not found"));
        review.setIsActive(false);
        reviewRepository.save(review);
    }

    private ReviewResponseDto mapToDto(Review review){
        ReviewResponseDto dto=new ReviewResponseDto();
        dto.setReviewId(review.getReviewId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setReviewDate(review.getReviewDate());
        dto.setUserId(review.getUser().getUserId());
        dto.setBookId(review.getBook().getBookId());

        return dto;
    }
}
