package com.tss.bookstore.repository;

import com.tss.bookstore.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher,Long> {
    Optional<Publisher> findByPublisherIdAndIsActiveTrue(Long publisherId);

    Page<Publisher> findByIsActiveTrue(Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndPublisherIdNot(
            String email,
            Long publisherId
    );
}
