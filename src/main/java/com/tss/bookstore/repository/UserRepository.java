package com.tss.bookstore.repository;

import com.tss.bookstore.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.mapstruct.control.MappingControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<User> findByUserIdAndIsActiveTrue(Long userId);
    Page<User> findByIsActiveTrue(Pageable pageable);
    boolean existsByEmailAndUserIdNot(String email,Long userId);

    @Query("""
    SELECT u
    FROM User u
    LEFT JOIN FETCH u.userProfile
    WHERE u.isActive = true
    """)
    List<User> findAllWithProfile();

    @Query("""
    SELECT u
    FROM User u
    LEFT JOIN FETCH u.userProfile
    WHERE u.isActive = true and u.userId=:userId
    """)
    Optional<User> findByIdWithProfile(Long userId);

    @EntityGraph(attributePaths = "userProfile")
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    Page<User> findActiveUsersWithProfile(Pageable pageable);
}
