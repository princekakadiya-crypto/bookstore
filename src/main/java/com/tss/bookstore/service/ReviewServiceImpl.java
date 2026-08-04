package com.tss.bookstore.service;

import com.tss.bookstore.dto.PageDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    private static final Logger log= LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Override
    public ReviewResponseDto addReview(ReviewRequestDto request){
        log.info(
                "Adding review. bookId={}, userId={}",
                request.getBookId(),
                request.getUserId()
        );

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

        Review savedReview=reviewRepository.save(review);

        log.info(
                "Review added successfully. reviewId={}, bookId={}, userId={}",
                savedReview.getReviewId(),
                savedReview.getBook().getBookId(),
                savedReview.getUser().getUserId()
        );

        return mapToDto(savedReview);
    }

    @Override
    public ReviewResponseDto getReviewById(Long id){
        log.debug("Fetching review. reviewId={}", id);

        Review review = reviewRepository.findByReviewIdAndIsActiveTrue(id).orElseThrow(
                ()->new NotFoundException("Review not found"));

        return mapToDto(review);

    }

    @Override
    public PageDto<ReviewResponseDto> getAllReviews(Pageable pageable) {

        log.debug(
                "Fetching reviews. page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Review> reviews = reviewRepository.findByIsActiveTrue(pageable);

        List<ReviewResponseDto> responseDtos = new ArrayList<>();

        for (Review review : reviews.getContent()) {
            ReviewResponseDto dto = mapToDto(review);
            responseDtos.add(dto);
        }

        PageDto<ReviewResponseDto> pageDto = new PageDto<>();

        pageDto.setContent(responseDtos);
        pageDto.setCurrentPage(reviews.getNumber());
        pageDto.setPageSize(reviews.getSize());
        pageDto.setTotalPages(reviews.getTotalPages());
        pageDto.setTotalElements(reviews.getTotalElements());
        pageDto.setFirst(reviews.isFirst());
        pageDto.setLast(reviews.isLast());
        pageDto.setEmpty(reviews.isEmpty());

        return pageDto;
    }
    @Override
    public ReviewResponseDto updateReview(Long id, ReviewRequestDto request){
        log.info("Updating review. reviewId={}", id);
        Review review = reviewRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Review not found"));
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview=reviewRepository.save(review);
        log.info("Review updated successfully. reviewId={}", id);
        return mapToDto(savedReview);

    }

    @Override
    public void deleteReview(Long id){
        log.info("Soft deleting review. reviewId={}", id);
        Review review = reviewRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Review not found"));
        review.setIsActive(false);
        reviewRepository.save(review);
        log.info("Review deleted successfully. reviewId={}", id);
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
