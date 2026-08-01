package com.tss.bookstore.repository;

import com.tss.bookstore.entity.Order;
import com.tss.bookstore.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findAll(Pageable pageable);

    List<Order> findByUserUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);
}
