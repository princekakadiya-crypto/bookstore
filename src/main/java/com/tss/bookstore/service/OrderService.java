package com.tss.bookstore.service;

import com.tss.bookstore.dto.OrderRequestDto;
import com.tss.bookstore.dto.OrderResponseDto;
import com.tss.bookstore.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto requestDto);

    OrderResponseDto getOrderById(Long orderId);

    Page<OrderResponseDto> getAllOrders(Pageable pageable);

    List<OrderResponseDto> getOrdersByUser(Long userId);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status);

    void cancelOrder(Long orderId);
}
