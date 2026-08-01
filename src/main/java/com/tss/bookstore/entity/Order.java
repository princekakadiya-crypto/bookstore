package com.tss.bookstore.entity;

import com.tss.bookstore.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Column(name = "order_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Column
    private LocalDate orderDate;
    @Column
    private OrderStatus status;
    @Column
    private Double totalAmount;

    @OneToMany(mappedBy = "order",
        cascade = CascadeType.ALL
    )
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
