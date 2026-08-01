package com.tss.bookstore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserProfileResponseDto {
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String avatar;
}
