package com.tss.bookstore.dto;

import lombok.Data;

@Data
public class PublisherResponseDto {
    private Long publisherId;
    private String name;
    private String email;
    private String phone;
    private String address;
}
