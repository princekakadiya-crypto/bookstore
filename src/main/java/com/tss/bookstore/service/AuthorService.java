package com.tss.bookstore.service;

import com.tss.bookstore.dto.AuthorRequestDto;
import com.tss.bookstore.dto.AuthorResponseDto;
import com.tss.bookstore.dto.BookResponseDto;
import com.tss.bookstore.dto.PageDto;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorResponseDto addAuthor(AuthorRequestDto dto);
    AuthorResponseDto updateAuthor(Long authorId, AuthorRequestDto dto);
    AuthorResponseDto getAuthorById(Long authorId);
    PageDto<AuthorResponseDto> getAllAuthors(Pageable pageable);
    void deleteAuthor(Long authorId);
}
