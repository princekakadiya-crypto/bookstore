package com.tss.bookstore.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ErrorResponseDto {
    private Integer code;
    private String errorMessage;
    private LocalDateTime errorTime;
    private String path;
}
