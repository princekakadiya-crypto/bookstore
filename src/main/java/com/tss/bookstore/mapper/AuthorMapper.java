package com.tss.bookstore.mapper;

import com.tss.bookstore.dto.AuthorRequestDto;
import com.tss.bookstore.dto.AuthorResponseDto;
import com.tss.bookstore.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toEntity(AuthorRequestDto dto);
    AuthorResponseDto toDto(Author author);
}
