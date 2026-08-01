package com.tss.bookstore.repository;

import com.tss.bookstore.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findByAuthorIdAndIsActiveTrue(Long authorId);
    Page<Author> findByIsActiveTrue(Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndAuthorIdNot(
            String name,
            Long authorId
    );
}
