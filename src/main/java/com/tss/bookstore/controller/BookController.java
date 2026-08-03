package com.tss.bookstore.controller;

import com.tss.bookstore.dto.BookRequestDto;
import com.tss.bookstore.dto.BookResponseDto;
import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.StockRequestDto;
import com.tss.bookstore.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDto> addBook(@RequestBody BookRequestDto dto) {
        return new ResponseEntity<>(bookService.addBook(dto), HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<PageDto<BookResponseDto>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        return ResponseEntity.ok(
                bookService.getAllBooks(title, categoryId, minPrice, maxPrice, pageable)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{bookId}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long bookId, @Valid @RequestBody StockRequestDto stockRequestDto) {
        bookService.updateStock(bookId, stockRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bookId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long bookId) {

        return ResponseEntity.ok(bookService.getAverageRating(bookId));
    }
}
