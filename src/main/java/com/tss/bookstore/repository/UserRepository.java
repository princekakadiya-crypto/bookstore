package com.tss.bookstore.repository;

import com.tss.bookstore.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<User> findByUserIdAndIsActiveTrue(Long userId);
    Page<User> findByIsActiveTrue(Pageable pageable);
    boolean existsByEmailAndUserIdNot(String email,Long userId);
}
