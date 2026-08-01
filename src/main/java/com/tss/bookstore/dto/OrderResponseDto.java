package com.tss.bookstore.dto;

import com.tss.bookstore.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long orderId;

    private LocalDate orderDate;

    private OrderStatus status;

    private Double totalAmount;

    private Long userId;

    private String userName;

    private List<OrderItemResponseDto> items;
}
