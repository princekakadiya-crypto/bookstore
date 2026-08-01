package com.tss.bookstore.repository;

import com.tss.bookstore.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByReviewIdAndIsActiveTrue(Long id);

    List<Review> findAllByBookBookIdAndIsActiveTrue(Long bookId);

    List<Review> findAllByUserUserIdAndIsActiveTrue(Long userId);
}
