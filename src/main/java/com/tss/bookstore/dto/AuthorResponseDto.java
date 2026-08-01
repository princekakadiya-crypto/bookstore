package com.tss.bookstore.dto;

import lombok.Data;

@Data
public class AuthorResponseDto {
    private Long authorId;
    private String name;
    private String country;
}
