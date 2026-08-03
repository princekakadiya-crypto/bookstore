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

    boolean existsByISBN(String ISBN);

    boolean existsByISBNAndBookIdNot(String ISBN,Long bookId);

    Page<Book> findByAuthorsAuthorId(Long authorId, Pageable pageable);

    @Query(value = """
        SELECT DISTINCT b.*
        FROM book b
        LEFT JOIN category c 
            ON b.category_id = c.category_id
        LEFT JOIN book_author ba 
            ON b.book_id = ba.book_id
        LEFT JOIN author a 
            ON ba.author_id = a.author_id
        WHERE
            (:title IS NULL OR b.title ILIKE '%' || :title || '%')
            AND (:categoryId IS NULL OR c.category_id = :categoryId)
            AND (:category IS NULL OR c.name ILIKE '%' || :category || '%')
            AND (:authorId IS NULL OR a.author_id = :authorId)
            AND (:author IS NULL OR a.name ILIKE '%' || :author || '%')
            AND (:minPrice IS NULL OR b.price >= :minPrice)
            AND (:maxPrice IS NULL OR b.price <= :maxPrice)
            AND (:inStock IS NULL OR 
                 (:inStock = true AND b.stock > 0)
                 OR (:inStock = false AND b.stock = 0))
        """, nativeQuery = true)
    Page<Book> searchBooks(
            @Param("title") String title,
            @Param("categoryId") Long categoryId,
            @Param("category") String category,
            @Param("authorId") Long authorId,
            @Param("author") String author,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("inStock") Boolean inStock,
            Pageable pageable
    );
}
