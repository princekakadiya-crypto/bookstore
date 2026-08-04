package com.tss.bookstore.exception;

import com.tss.bookstore.error.ErrorResponseDto;
import com.tss.bookstore.error.ErrorValidationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> NotFoundException(NotFoundException notFoundException, HttpServletRequest request){

        logger.warn(
                "Resource not found: {} | URI: {}",
                notFoundException.getMessage(),
                request.getRequestURI()
        );

        ErrorResponseDto error=new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                notFoundException.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDto> duplicateResourceException(DuplicateResourceException duplicateResourceException, HttpServletRequest request){

        logger.warn(
                "Duplicate Resource : {} | URI: {}",
                duplicateResourceException.getMessage(),
                request.getRequestURI()
        );

        ErrorResponseDto error=new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                duplicateResourceException.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponseDto> invalidOrderStateException(InvalidOrderStateException invalidOrderStateException, HttpServletRequest request){

        logger.warn(
                "Invalid state: {} | URI: {}",
                invalidOrderStateException.getMessage(),
                request.getRequestURI()
        );

        ErrorResponseDto error=new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                invalidOrderStateException.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponseDto> InsufficientStockException(InsufficientStockException insufficientStockException, HttpServletRequest request){

        logger.warn(
                "Insufficient Stock : {} | URI: {}",
                insufficientStockException.getMessage(),
                request.getRequestURI()
        );

        ErrorResponseDto error=new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                insufficientStockException.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentException(IllegalArgumentException illegalArgumentException, HttpServletRequest request){

        logger.warn(
                "Invalid argument: {} | URI: {}",
                illegalArgumentException.getMessage(),
                request.getRequestURI()
        );

        ErrorResponseDto error=new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                illegalArgumentException.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorValidationResponse> handleValidationException(MethodArgumentNotValidException exception,HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        logger.warn(
                "Validation failed: {} | URI: {}",
                errors,
                request.getRequestURI()
        );

        ErrorValidationResponse response = new ErrorValidationResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                LocalDateTime.now(),
                errors
        );
        return  new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(
            Exception exception,
            HttpServletRequest request
    ){

        logger.error(
                "Unexpected error occurred: {}",
                exception.getMessage(),
                exception
        );

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
