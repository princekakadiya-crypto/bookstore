package com.tss.bookstore.controller;


import com.tss.bookstore.dto.AuthorRequestDto;
import com.tss.bookstore.dto.AuthorResponseDto;
import com.tss.bookstore.dto.BookResponseDto;
import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.service.AuthorService;
import com.tss.bookstore.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/app/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<AuthorResponseDto> addAuthor(@Valid @RequestBody AuthorRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.addAuthor(dto));
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(
            @PathVariable Long authorId,
            @Valid @RequestBody AuthorRequestDto dto) {

        return ResponseEntity.ok(
                authorService.updateAuthor(authorId, dto)
        );
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> getAuthor(@PathVariable Long authorId) {

        return ResponseEntity.ok(
                authorService.getAuthorById(authorId)
        );
    }

    @GetMapping
    public ResponseEntity<PageDto<AuthorResponseDto>> getAuthors(Pageable pageable) {

        return ResponseEntity.ok(
                authorService.getAllAuthors(pageable)
        );
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {

        authorService.deleteAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{authorId}/books")
    public ResponseEntity<PageDto<BookResponseDto>> getBooksOfAuthor(@PathVariable Long authorId,Pageable pageable){
        return ResponseEntity.ok(
                bookService.getBooksBuAuthorId(authorId,pageable)
        );
    }
}
