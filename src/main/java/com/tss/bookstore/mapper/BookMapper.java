package com.tss.bookstore.mapper;

import com.tss.bookstore.dto.BookRequestDto;
import com.tss.bookstore.dto.BookResponseDto;
import com.tss.bookstore.entity.Author;
import com.tss.bookstore.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookRequestDto dto);

    BookResponseDto toDto(Book book);
}
