package com.tss.bookstore.service;

import com.tss.bookstore.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookResponseDto addBook(BookRequestDto dto);

    BookResponseDto updateBook(Long bookId, BookRequestDto dto);

    BookResponseDto getBookById(Long bookId);

    PageDto<BookDetailsResponseDto> getAllBooksDetails(String title, Long categoryId,String category,Long authorId,String author, Double minPrice, Double maxPrice,Boolean inStock,Pageable pageable);

    PageDto<BookResponseDto> getAllBooks(String title, Long categoryId,String category,Long authorId,String author, Double minPrice, Double maxPrice,Boolean inStock,Pageable pageable);

    void deleteBook(Long bookId);

    PageDto<BookResponseDto> getBooksBuAuthorId(Long authorId, Pageable pageable);

    void updateStock(Long bookId, StockRequestDto stockRequestDto);

    Double getAverageRating(Long bookId);

}
