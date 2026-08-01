package com.tss.bookstore.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    @NotNull(message = "User Id is required")
    private Long userId;

    @NotEmpty(message = "Order must contain at least one book")
    @Valid
    private List<OrderItemRequestDto> items;
}
