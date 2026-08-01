package com.tss.bookstore.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorValidationResponse {
    private Integer code;
    private String errorMessage;
    private LocalDateTime errorTime;
    private Map<String,String> errors;
}
