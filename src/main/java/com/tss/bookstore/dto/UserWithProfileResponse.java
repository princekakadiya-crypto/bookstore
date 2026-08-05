package com.tss.bookstore.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserWithProfileResponse {
    private Long userId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private UserProfileResponseDto profile;
}
