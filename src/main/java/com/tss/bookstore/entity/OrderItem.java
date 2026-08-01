package com.tss.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_order_book",
                        columnNames = {"order_id", "book_id"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {
    @Column(name = "order_item_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    @Column
    private Integer quantity;
    @Column
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
