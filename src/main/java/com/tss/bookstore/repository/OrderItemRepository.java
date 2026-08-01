package com.tss.bookstore.repository;

import com.tss.bookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrderOrderId(Long orderId);

    Optional<OrderItem> findByOrderOrderIdAndBookBookId(Long orderId, Long bookId);
}
