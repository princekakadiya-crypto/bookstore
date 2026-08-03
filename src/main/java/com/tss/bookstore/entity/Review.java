package com.tss.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "review",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_book_review",
                        columnNames = {"user_id", "book_id"}
                )
        })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {
    @Column(name = "review_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @Column
    private Integer rating;
    @Column
    private String comment;
    @Column
    private LocalDate reviewDate;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive=true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
