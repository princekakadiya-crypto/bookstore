package com.tss.bookstore.mapper;

import com.tss.bookstore.dto.OrderItemResponseDto;
import com.tss.bookstore.dto.OrderResponseDto;
import com.tss.bookstore.entity.Order;
import com.tss.bookstore.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "orderItems", target = "items")
    OrderResponseDto entityToDto(Order order);

    List<OrderResponseDto> entityToDto(List<Order> orders);

    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    OrderItemResponseDto entityToDto(OrderItem orderItem);

    List<OrderItemResponseDto> orderItemsToDto(List<OrderItem> orderItems);
}
