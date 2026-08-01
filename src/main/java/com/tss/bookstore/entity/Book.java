package com.tss.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
    @Column(name = "book_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    @Column
    private String title;
    @Column
    private Double price;
    @Column
    private Integer stock;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive=true;

    @ManyToMany()
    @JoinTable(name = "book_author",
        joinColumns =@JoinColumn(name = "book_id"),
            inverseJoinColumns =@JoinColumn(name = "author_id"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_book_author",
                            columnNames = {"book_id", "author_id"}
                    )
            }
    )
    private Set<Author> authors;

    @ManyToOne()
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "book")
    private List<Review> reviews;
}
