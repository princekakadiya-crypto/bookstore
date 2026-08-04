package com.tss.bookstore.repository;

import com.tss.bookstore.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByReviewIdAndIsActiveTrue(Long id);

    List<Review> findAllByBookBookIdAndIsActiveTrue(Long bookId);

    List<Review> findAllByUserUserIdAndIsActiveTrue(Long userId);

    @Query("""
            SELECT COALESCE(AVG(r.rating), 0)
            FROM Review r
            WHERE r.book.bookId = :bookId
            AND r.isActive = true
            """)
    Double getAverageRating(@Param("bookId") Long bookId);

    Page<Review> findByIsActiveTrue(Pageable pageable);
}
