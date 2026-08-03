package com.tss.bookstore.repository;

import com.tss.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByBookIdAndIsActiveTrue(Long bookId);

    Page<Book> findByIsActiveTrue(Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);

    Page<Book> findByAuthorsAuthorId(Long authorId, Pageable pageable);

    boolean existsByTitleIgnoreCaseAndBookIdNot(String title, Long bookId);
}
