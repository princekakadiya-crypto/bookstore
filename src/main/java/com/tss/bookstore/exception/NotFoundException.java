package com.tss.bookstore.exception;

import lombok.AllArgsConstructor;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
