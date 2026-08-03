package com.tss.bookstore.repository;

import com.tss.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>{
    Optional<Book> findByBookIdAndIsActiveTrue(Long bookId);

    Page<Book> findByIsActiveTrue(Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);

    Page<Book> findByAuthorsAuthorId(Long authorId, Pageable pageable);

    @Query(value = """
    SELECT *
    FROM book b
    WHERE
        (:title IS NULL OR b.title ILIKE '%' || :title || '%')
        AND (:categoryId IS NULL OR b.category_id = :categoryId)
        AND (:minPrice IS NULL OR b.price >= :minPrice)
        AND (:maxPrice IS NULL OR b.price <= :maxPrice)
    """,
            nativeQuery = true)
    Page<Book> searchBooks(
            @Param("title") String title,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
