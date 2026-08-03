package com.tss.bookstore.service;

import com.tss.bookstore.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookResponseDto addBook(BookRequestDto dto);

    BookResponseDto updateBook(Long bookId, BookRequestDto dto);

    BookResponseDto getBookById(Long bookId);

    PageDto<BookResponseDto> getAllBooks(String title, Long categoryId, Double minPrice, Double maxPrice,Pageable pageable);

    void deleteBook(Long bookId);

    PageDto<BookResponseDto> getBooksBuAuthorId(Long authorId, Pageable pageable);

    void updateStock(Long bookId, StockRequestDto stockRequestDto);

    Double getAverageRating(Long bookId);

}
